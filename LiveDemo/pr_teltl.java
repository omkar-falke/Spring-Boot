import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.StringTokenizer;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
class pr_teltl extends cl_pbase
{
    private JTextField txtLOTNO,txtBAGQT,txtPRDCD,txtPRDDS,txtLOTDT;
    private cl_JTable tblBTDTL;
    private final int TBL_CHKFL = 0;
    private final int TBL_BATNO = 1;
    private final int TBL_RCTNO = 2;
    private final int TBL_BSTDT = 3;
    private final int TBL_BENDT = 4;
    private final int TBL_AVLQT = 5;
    private final int TBL_BAGQT = 6;
    private String strPRDTP = "01";
    private String strLINNO = "81";
    private String strYRDGT ="8";
    private String strCLPRD ="3";
    private String strSEPRD ="3";
    private String strPSTDT,strPENDT,strCLSFL,strLOTNO;
    private TBLINVFR objTBLVRF;
    private ButtonGroup btgLOTTP;    
	private JRadioButton rdbCLLOT;	 
	private JRadioButton rdbSELOT;	
    pr_teltl()
    {
        super(2);
       	setMatrix(14,7);
		setVGAP(15);
		
	
		add(rdbCLLOT=new JRadioButton("CL Lot",true),1,2,1,1,this,'L');
		add(rdbSELOT=new JRadioButton("SE Lot"),1,3,1,2,this,'L');
		btgLOTTP=new ButtonGroup();	
		btgLOTTP.add(rdbCLLOT);
		btgLOTTP.add(rdbSELOT);
		add(new JLabel("Lot Number"),2,1,1,1,this,'L');
		add(txtLOTNO = new TxtNumLimit(8.0),2,2,1,1,this,'L');
		add(new JLabel("Lot Date"),2,3,1,1,this,'L');
		add(txtLOTDT = new TxtDate(),2,4,1,1,this,'L');
		add(new JLabel("Grade"),2,5,1,1,this,'L');
		add(txtPRDCD = new TxtNumLimit(10.0),2,6,1,1,this,'L');
		add(txtPRDDS = new TxtLimit(45),2,7,1,1.94,this,'L');
		add(new JLabel("Lot Quantity"),3,1,1,1,this,'L');
		add(txtBAGQT = new TxtNumLimit(6.3),3,2,1,1,this,'L');
		String[] L_COLHD = {"Select","Batch No.","Reactor No","Start Date/Time","End Date/Time","Prd. Qty","Bag Qty"};
  		int[] L_COLSZ = {30,90,90,130,130,85,85};	    				
		tblBTDTL = crtTBLPNL1(this,L_COLHD,500,5,1,7,6,L_COLSZ,new int[]{0});	
		tblBTDTL.addMouseListener(this);		
		objTBLVRF = new TBLINVFR();
		tblBTDTL.setInputVerifier(objTBLVRF);	

    }
    void setENBL(boolean L_flgSTAT)
    {
        super.setENBL(L_flgSTAT);
        txtPRDDS.setEnabled(false);
        txtLOTNO.setEnabled(true);
        rdbCLLOT.setEnabled(true);
        rdbSELOT.setEnabled(true);
        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
        {
             txtPRDCD.setEnabled(true);
             tblBTDTL.cmpEDITR[TBL_BAGQT].setEnabled(true);
        }
        txtBAGQT.setEnabled(false);
        tblBTDTL.cmpEDITR[TBL_BATNO].setEnabled(false);
        tblBTDTL.cmpEDITR[TBL_RCTNO].setEnabled(false);
        tblBTDTL.cmpEDITR[TBL_BSTDT].setEnabled(false);
        tblBTDTL.cmpEDITR[TBL_BENDT].setEnabled(false);
        tblBTDTL.cmpEDITR[TBL_AVLQT].setEnabled(false);
    }
    public void actionPerformed(ActionEvent L_AE)
	{
	    super.actionPerformed(L_AE);
	    if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
	    {
	        if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
    	    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
    	       getPRVLT();
	    }
	    if((M_objSOURC == rdbCLLOT)||(M_objSOURC == rdbSELOT))
	    {
	     if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
	        getPRVLT();
	     else
	        clrCOMP();   
	    }
	}
	public void mouseReleased(MouseEvent L_ME)
	{
	    super.mouseReleased(L_ME);
   		if(L_ME.getSource().equals(tblBTDTL))
        {
          // System.out.println("Mouse event table"); 
            calPRDQT();
        }
	}
    public void keyPressed(KeyEvent L_KE)
	{
	    super.keyPressed(L_KE);
	    if (L_KE.getKeyCode()== L_KE.VK_ENTER)
		{
		    if(M_objSOURC == txtLOTNO)
	        {
	            if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
	            {
	                getDATA();
                    txtLOTDT.requestFocus();
    	            setMSG("Enter Lot Date..",'N');
    	            
                }
	            else
	                getDATA();            
	        }
	        if(M_objSOURC == txtLOTDT)
	        {
	            txtPRDCD.requestFocus();
    	        setMSG("Enter Grade..",'N');
	        }
	        if(M_objSOURC == txtPRDCD)
	        {
	            txtBAGQT.requestFocus();
	            setMSG("Enter Bagged Quantity..",'N');
	            getBATDT(0);
	        }
	   	}
	    if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
		    if(M_objSOURC == txtLOTNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtLOTNO";
				String L_ARRHDR[] = {"Lot No", "Grade"};
			    M_strSQLQRY = "Select LT_LOTNO,PR_PRDDS from PR_LTMST,CO_PRMST WHERE LT_PRDCD = PR_PRDCD AND LT_STSFL <>'X' AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ";
			    if(rdbCLLOT.isSelected())
                {
                    M_strSQLQRY +=" substr(LT_LOTNO,3,1) ='"+strCLPRD+"'";
                }
                else
                {
                     M_strSQLQRY +=" substr(LT_LOTNO,3,1) ='"+strSEPRD+"'";
                }
				M_strSQLQRY += " Order by LT_LOTNO DESC ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			}
            if(M_objSOURC == txtPRDCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPRDCD";
				String L_ARRHDR[] = {"Code", "Description"};
			    M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST WHERE ";
			     if(rdbCLLOT.isSelected())
                {
                    M_strSQLQRY +=" substr(PR_PRDCD,1,6) ='512101' ";
                }
                else
                {
                    M_strSQLQRY +=" substr(PR_PRDCD,1,6) ='512102' ";
                }
			    
				M_strSQLQRY += " Order by pr_prdds ";
				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT");
			}
		}
	}
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtPRDCD"))
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			L_STRTKN.nextToken();
			txtPRDCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
			txtPRDDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		if(M_strHLPFLD.equals("txtLOTNO"))
		{
		    txtLOTNO.setText(cl_dat.M_strHLPSTR_pbst);
		}
	}
	void getBATDT(int L_intROWNO)
	{
	    java.sql.Timestamp L_TMPTM;
        String L_strPSTDT="";
        String L_strPENDT ="";
        String L_strBATLS ="";
        try
        {
            if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
            {
                boolean L_FIRST = false;
                for(int i=0;i<tblBTDTL.getRowCount();i++)
        	    {
        	        if(nvlSTRVL(tblBTDTL.getValueAt(i,TBL_BATNO).toString(),"").length() >0)
        	        {
        	            if(!L_FIRST)
        	            {
        	                L_FIRST = true;
        	                L_strBATLS = "'"+tblBTDTL.getValueAt(i,TBL_BATNO).toString()+"'";
        	            }
        	            else           
        	                L_strBATLS += ",'"+tblBTDTL.getValueAt(i,TBL_BATNO).toString()+"'";
        	        }
        	    }
            }
            M_strSQLQRY = "SELECT BT_BATNO,BT_BSTDT,BT_BENDT,BT_RCTNO,(IFNULL(BT_PRDQT,0) - IFNULL(BT_BAGQT,0))L_AVLQT FROM PR_BTMST ";
            M_strSQLQRY += " WHERE BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BT_GRDCD ='"+txtPRDCD.getText().trim()+"' AND IFNULL(BT_PRDQT,0) - IFNULL(BT_BAGQT,0) >0 ";
            if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
            {
                M_strSQLQRY += " AND BT_BATNO NOT IN("+L_strBATLS +")";
            }
            if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
            {
                M_strSQLQRY += " order by bt_batno";
            }
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
            int i;
            i = L_intROWNO;
            if(M_rstRSSET !=null)
            {
                while(M_rstRSSET.next())
                {
                    tblBTDTL.setValueAt(M_rstRSSET.getString("BT_BATNO"),i,TBL_BATNO);
                    tblBTDTL.setValueAt(M_rstRSSET.getString("BT_RCTNO"),i,TBL_RCTNO);
                    tblBTDTL.setValueAt(M_rstRSSET.getString("L_AVLQT"),i,TBL_AVLQT);                 
                    tblBTDTL.setValueAt(M_rstRSSET.getString("L_AVLQT"),i,TBL_BAGQT);                 
                    L_TMPTM = M_rstRSSET.getTimestamp("BT_BSTDT");
    				if(L_TMPTM !=null)
    					L_strPSTDT =M_fmtLCDTM.format(M_rstRSSET.getTimestamp("BT_BSTDT"));
    				if(i ==0)
    				    strPSTDT = L_strPSTDT;	
    				L_TMPTM = M_rstRSSET.getTimestamp("BT_BENDT");
    				if(L_TMPTM !=null)
    				{
    					L_strPENDT =M_fmtLCDTM.format(M_rstRSSET.getTimestamp("BT_BENDT"));
    				}
    				if(L_strPSTDT.trim().length() > 0)
    				{
    				    tblBTDTL.setValueAt(L_strPSTDT,i,TBL_BSTDT);
    				}
    				if(L_strPENDT.trim().length() > 0)
    				{
    				    tblBTDTL.setValueAt(L_strPENDT,i,TBL_BENDT);
    				    strPENDT = L_strPENDT;
    				}
                    i++;
                }
            }
        }
        catch(Exception L_E)
        {
            setMSG(L_E,"VK_ENTER");
        }
	}
	void getDATA()
	{
	    try
        {
            java.sql.Timestamp L_TMPTM;
            java.sql.Date L_TMPDT;
            String L_strTEMP ="";
            strCLSFL ="";
            M_strSQLQRY = "SELECT LT_LOTNO,LT_PRDCD,LT_PRDQT,LT_CLSFL,Date(LT_PSTDT)L_LOTDT,PR_PRDDS,BT_BATNO,BT_BSTDT,BT_BENDT,BT_RCTNO,BT_PRDQT,LTB_BAGQT FROM"
                        + " PR_LTMST,co_prmst,PR_BTMST,PR_LTBDT WHERE LT_PRDCD = PR_PRDCD AND LT_PRDCD = BT_GRDCD AND LT_CMPCD = BT_CMPCD AND LT_LOTNO = LTB_LOTNO AND LT_CMPCD = LTB_CMPCD "
                        + " AND BT_BATNO = LTB_BATNO AND BT_CMPCD = LTB_CMPCD AND BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BT_BATNO <>'00000000' and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO ='"+txtLOTNO.getText().trim()+"'";
            if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
            {
                M_strSQLQRY = "SELECT LT_LOTNO,LT_PRDCD,LT_PRDQT,LT_CLSFL,Date(LT_PSTDT)L_LOTDT,PR_PRDDS,BT_BATNO,BT_BSTDT,BT_BENDT,BT_RCTNO,(ifnull(BT_PRDQT,0) - ifnull(BT_BAGQT,0))+ifnull(LTB_BAGQT,0) BT_PRDQT,LTB_BAGQT FROM"
                        + " PR_LTMST,co_prmst,PR_BTMST,PR_LTBDT WHERE LT_PRDCD = PR_PRDCD AND LT_PRDCD = BT_GRDCD AND LT_CMPCD = BT_CMPCD AND LT_LOTNO = LTB_LOTNO AND LT_CMPCD = LTB_CMPCD "
                        + " AND BT_BATNO = LTB_BATNO AND BT_CMPCD = LTB_CMPCD AND BT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BT_BATNO <>'00000000' AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO ='"+txtLOTNO.getText().trim()+"'";
            }
            if(tblBTDTL.isEditing())
    			tblBTDTL.getCellEditor().stopCellEditing();
    	    tblBTDTL.setRowSelectionInterval(0,0);
    	    tblBTDTL.setColumnSelectionInterval(0,0);
              M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
            int i=0;
            String L_strLOTNO = txtLOTNO.getText().trim();
            if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
       	    {
                  cl_dat.M_btnSAVE_pbst.setEnabled(true);
       	    }
       	    else
       	    {
       	        clrCOMP();
    			tblBTDTL.clrTABLE();
    			txtLOTNO.setText(L_strLOTNO);
       	    }
            if(M_rstRSSET !=null)
            {
                while(M_rstRSSET.next())
                {   
                    if(i==0)
                    {
                        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
        			    {
                              setMSG("Lot Already exists..",'E');
                              cl_dat.M_btnSAVE_pbst.setEnabled(false);
                              txtLOTNO.requestFocus();
                              return;   			        
        			    }
                        strCLSFL = nvlSTRVL(M_rstRSSET.getString("LT_CLSFL"),"");
                        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
                        {
                            if(strCLSFL.equals("9"))
                            {
                                setMSG("Lot Modification is not allowed at this stage..",'E');
                                return;
                            }
                        }
                        txtLOTNO.setText(M_rstRSSET.getString("LT_LOTNO"));       
                        txtPRDCD.setText(M_rstRSSET.getString("LT_PRDCD"));
                        txtPRDDS.setText(M_rstRSSET.getString("PR_PRDDS"));
                        txtBAGQT.setText(M_rstRSSET.getString("LT_PRDQT"));
                        L_TMPDT = M_rstRSSET.getDate("L_LOTDT");
        				if(L_TMPDT !=null)
        					txtLOTDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("L_LOTDT")));
                    }
                    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
                        tblBTDTL.setValueAt(new Boolean("true"),i,TBL_CHKFL);
                    tblBTDTL.setValueAt(M_rstRSSET.getString("BT_BATNO"),i,TBL_BATNO);
                    tblBTDTL.setValueAt(M_rstRSSET.getString("BT_RCTNO"),i,TBL_RCTNO);
                    tblBTDTL.setValueAt(M_rstRSSET.getString("BT_PRDQT"),i,TBL_AVLQT);
                    tblBTDTL.setValueAt(M_rstRSSET.getString("LTB_BAGQT"),i,TBL_BAGQT);
                   
                    L_TMPTM = M_rstRSSET.getTimestamp("BT_BSTDT");
    				L_strTEMP ="";
    				if(L_TMPTM !=null)
    				{
    					L_strTEMP =M_fmtLCDTM.format(M_rstRSSET.getTimestamp("BT_BSTDT"));
    				}
    				if(L_strTEMP.trim().length() > 0)
    				    tblBTDTL.setValueAt(L_strTEMP,i,TBL_BSTDT);
    				L_strTEMP ="";
                    L_TMPTM = M_rstRSSET.getTimestamp("BT_BENDT");
    				if(L_TMPTM !=null)
    					L_strTEMP =M_fmtLCDTM.format(M_rstRSSET.getTimestamp("BT_BENDT"));
    				if(L_strTEMP.trim().length() > 0)
    				    tblBTDTL.setValueAt(L_strTEMP,i,TBL_BENDT);
    			    i++;
                }
                M_rstRSSET.close();
                if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
                    getBATDT(i);
            }
        }
        catch(Exception L_E)
        {
            setMSG(L_E,"VK_ENTER");
        }
	}
	boolean vldDATA()
	{
	    if(tblBTDTL.isEditing())
			tblBTDTL.getCellEditor().stopCellEditing();
	    tblBTDTL.setRowSelectionInterval(0,0);
	    tblBTDTL.setColumnSelectionInterval(0,0);
	    setMSG("",'N');
	    if(txtLOTNO.getText().length() ==0)
	    {
	        setMSG("Enter Lot No..",'E');
	        return false;
	    }
	    if(txtLOTDT.getText().length() ==0)
	    {
	        setMSG("Enter Lot Date..",'E');
	        return false;
	    }
	    if(txtPRDCD.getText().length() ==0)
	    {
	        setMSG("Enter Grade..",'E');
	        return false;
	    }
	    
	    double L_dblBAGQT =0;
	    int L_intSELCT =0;
	    for(int i=0;i<tblBTDTL.getRowCount();i++)
	    {
	        if(tblBTDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
	        {
	            L_dblBAGQT += Double.parseDouble(tblBTDTL.getValueAt(i,TBL_BAGQT).toString());
	            L_intSELCT ++;
	        }
	    }
	    if(L_intSELCT == 0)
	    {
	        setMSG("Select Batch Details..",'E');
	        return false;
	    }
	    calPRDQT();
	    if(txtBAGQT.getText().length() ==0)
	    {
	        setMSG("Enter Bagged Qty..",'E');
	        return false;
	    }
	    if(Double.parseDouble(txtBAGQT.getText()) == 0)
	    {
	        setMSG("Enter Bagged Qty..",'E');
	        return false;
	    }
	    return true;
	}
	void exeSAVE()
	{
	    if(!vldDATA())
	        return;
	    cl_dat.M_flgLCUPD_pbst = true;    
	    try
	    {
    	   if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
    	   {
        	    M_strSQLQRY = "INSERT INTO PR_LTMST(LT_CMPCD,LT_SBSCD,LT_PRDTP,LT_LOTNO,LT_RCLNO,LT_PSTDT,LT_PENDT,LT_TPRCD,LT_PRDCD,LT_CPRCD,LT_PRDQT,LT_IPRDS,LT_CLSFL,LT_TRNFL,LT_STSFL,LT_LUSBY,LT_LUPDT) VALUES(";
        	    M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst +"',";
				M_strSQLQRY += "'"+M_strSBSCD+"',";
				M_strSQLQRY += "'"+strPRDTP +"',";
        	    M_strSQLQRY += "'"+txtLOTNO.getText().trim() +"','00',";
        	    M_strSQLQRY += "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtLOTDT.getText().trim()+" 00:00"))+"',";
        	    M_strSQLQRY += "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtLOTDT.getText().trim()+" 00:00"))+"',";
        	    M_strSQLQRY += "'"+txtPRDCD.getText().trim() +"',";
        	    M_strSQLQRY += "'"+txtPRDCD.getText().trim() +"',";
        	    M_strSQLQRY += "'"+txtPRDCD.getText().trim() +"',";
        	    M_strSQLQRY += txtBAGQT.getText().trim() +",";
        	    M_strSQLQRY += "'"+txtPRDDS.getText().trim() +"',";
        	    M_strSQLQRY += "'9',";
        	    M_strSQLQRY += getUSGDTL("LT",'I',"0")+")";
        	    cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
        	    for(int i=0;i<tblBTDTL.getRowCount();i++)
        	    {
        	        if(tblBTDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
        	        {
                	    M_strSQLQRY = "INSERT INTO PR_LTBDT(LTB_CMPCD,LTB_SBSCD,LTB_BATNO,LTB_LOTNO,LTB_PRDCD,LTB_BAGQT,LTB_TRNFL,LTB_STSFL,LTB_LUSBY,LTB_LUPDT) VALUES(";
                	    M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY += "'"+M_strSBSCD+"',";
						M_strSQLQRY += "'"+tblBTDTL.getValueAt(i,TBL_BATNO).toString() +"',";
                	    M_strSQLQRY += "'"+txtLOTNO.getText().trim() +"',";
                	    M_strSQLQRY += "'"+txtPRDCD.getText().trim() +"',";
                	    M_strSQLQRY += tblBTDTL.getValueAt(i,TBL_BAGQT).toString() +",";
                	    M_strSQLQRY += getUSGDTL("LTB",'I',"0")+")";
                	    cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
            	    }
        	    }
        	    //cl_dat.exeSRLSET("D"+cl_dat.M_strCMPCD_pbst+"","PRXXLOT",strLOTNO.substring(0,4),strLOTNO.substring(4));
        	    cl_dat.exeSRLSET("D"+cl_dat.M_strCMPCD_pbst,"PRXXLOT",txtLOTNO.getText().trim().substring(0,4),txtLOTNO.getText().substring(4));
        	    
    	   }
    	   else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
    	   {
    	        M_strSQLQRY = "UPDATE PR_LTMST SET ";
    	        M_strSQLQRY += "LT_TPRCD = '"+txtPRDCD.getText().trim() +"',";
        	    M_strSQLQRY += "LT_PRDCD = '"+txtPRDCD.getText().trim() +"',";
        	    M_strSQLQRY += "LT_CPRCD = '"+txtPRDCD.getText().trim() +"',";
        	    M_strSQLQRY += "LT_PRDQT = "+ txtBAGQT.getText().trim() +",";
        	    M_strSQLQRY += getUSGDTL("LT",'U',"0");    
        	    M_strSQLQRY += " WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP ='"+strPRDTP +"'";
        	    M_strSQLQRY += " AND LT_LOTNO ='"+txtLOTNO.getText().trim() +"' AND LT_RCLNO ='00'";
        	    cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
        	    
        	    M_strSQLQRY = "DELETE FROM PR_LTBDT ";
    	        M_strSQLQRY += " WHERE LTB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LTB_LOTNO ='"+txtLOTNO.getText().trim() +"' ";
    	        cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    	        for(int i=0;i<tblBTDTL.getRowCount();i++)
        	    {
        	        if(tblBTDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
        	        {
                	    M_strSQLQRY = "INSERT INTO PR_LTBDT(LTB_CMPCD,LTB_SBSCD,LTB_BATNO,LTB_LOTNO,LTB_PRDCD,LTB_BAGQT,LTB_TRNFL,LTB_STSFL,LTB_LUSBY,LTB_LUPDT) VALUES(";
                	    M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY += "'"+M_strSBSCD+"',";
						M_strSQLQRY += "'"+tblBTDTL.getValueAt(i,TBL_BATNO).toString() +"',";
                	    M_strSQLQRY += "'"+txtLOTNO.getText().trim() +"',";
                	    M_strSQLQRY += "'"+txtPRDCD.getText().trim() +"',";
                	    M_strSQLQRY += tblBTDTL.getValueAt(i,TBL_BAGQT).toString() +",";
                	    M_strSQLQRY += getUSGDTL("LTB",'I',"0")+")";
                	    cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
            	    }
        	    }
    	   }
    	    if(cl_dat.exeDBCMT("exeSAVE"))
            {
                clrCOMP();
                 setMSG("Data Saved ..",'N');
                if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
                    getPRVLT();
           }
            else
            {
                setMSG("Error in Saving ..",'E');
            }
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"exeSAVE");
	    }
	}
	void calPRDQT()
	{
	    double L_strPRDQT =0;
	    for(int i=0;i<tblBTDTL.getRowCount();i++)
	    {
	        if(tblBTDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
	        {
	            L_strPRDQT += Double.parseDouble(tblBTDTL.getValueAt(i,TBL_BAGQT).toString());
	        }
	    }
	    txtBAGQT.setText(setNumberFormat(L_strPRDQT,3));
	}
	private void getPRVLT()
   {
        try
        {
       	    if(rdbCLLOT.isSelected())
            {
                M_strSQLQRY ="Select (cmt_codcd||CMT_CCSVL) L_LOTNO,CMT_CHP02 from CO_CDTRN WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cGSTP ='PRXXLOT' AND ifnull(CMT_STSFL,'') <>'X' ";
			    M_strSQLQRY +=" AND CMT_CODCD = '"+strLINNO + strCLPRD + strYRDGT +"'";
            }
            else
            {
                M_strSQLQRY ="Select (cmt_codcd||CMT_CCSVL) L_LOTNO,CMT_CHP02 from CO_CDTRN WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cGSTP ='PRXXLOT' AND ifnull(CMT_STSFL,'') <>'X' ";
			    M_strSQLQRY +=" AND CMT_CODCD = '"+strLINNO + strSEPRD + strYRDGT +"'";
            }
            
			
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strLOTNO = "";
				int L_intLOTNO =0;
				while(M_rstRSSET.next())
				{
				    L_intLOTNO = M_rstRSSET.getInt("L_LOTNO");
				    L_intLOTNO++;
				    strLOTNO = String.valueOf(L_intLOTNO);
				    /*for(int i=0;i<(4 - strLOTNO.length());i++)
				    {
				        strLOTNO += "0";
				    }
				    strLOTNO += strLINNO + strPRDGT + strYRDGT;*/
				    txtLOTNO.setText(strLOTNO);
				    txtLOTNO.requestFocus();
			   	}
			}
        }
        catch(Exception L_E)
        {
            setMSG(L_E,"getPRVLT");
        }
   }
   
	private class TBLINVFR extends TableInputVerifier
    {
	    public boolean verify(int P_intROWID,int P_intCOLID)
		{
			String L_strTEMP ="";
			String strTEMP ="";
			double L_dblAVLQT =0;
			try
			{
				if(P_intCOLID==TBL_BAGQT)
				{
					strTEMP = tblBTDTL.getValueAt(P_intROWID,TBL_BAGQT).toString();
		    		if(strTEMP.length()>0)
					{
					    L_dblAVLQT = Double.parseDouble(tblBTDTL.getValueAt(P_intROWID,TBL_AVLQT).toString());
					    //L_dblAVLQT = L_dblAVLQT + (L_dblAVLQT * 0.1); // 10 % allowance
					    L_dblAVLQT = L_dblAVLQT + (L_dblAVLQT * 2); // 200 % allowance
						if(Double.parseDouble(strTEMP) <=0)
						{
							setMSG("Bagged qty. can not be Negative or Zero..",'E');
							return false;
						}
					/*	if(L_dblAVLQT < Math.abs(Double.parseDouble(strTEMP)))
					    {
							setMSG("Bagged Qty. is out of permissible range..",'E');
							return false;
					    }*/
					    calPRDQT();
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
