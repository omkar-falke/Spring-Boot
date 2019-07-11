import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.lang.*;
import java.util.Properties;
import java.util.*;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.*;
import java.util.StringTokenizer;
import java.io.File;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.*;


public class support extends JFrame implements ActionListener, KeyListener, FocusListener, ItemListener
{
        JLabel lblDTRDT, lblDTRSS, lblCURMSG, lblPRVMSG;
        JPanel pnlMAIN;
        String     LM_PTHWD, LM_DTRFL, LM_DTRFL1, LM_FLSUFX;
        String     LM_STRSQL, M_strSQLQRY, M_strSQLQRY1;
        File       LM_WDFILE;

        String     LM_CHKDT, LM_RETVL, LM_RTNSTR, LM_OPTVL;
        String     strOPT_REWORK_INVQT = "Invoice Quantity Reworking";
        String     strOPT_CRTTRG_INVQT = "Invoice Quantity Trigger";


        JButton btnRUN1, btnEXIT1;
        JTextField txtMSG, txtFILENM, txtLIBRNM;

        JComboBox cmbSELOPT;
        int LM_SCOL, LM_SROW, LM_PRVVL, LM_CURVL;
        int M_intUPDCT;

        ResultSet M_rstRSLSET;
        JOptionPane LM_OPTNPN;
        String M_LOGDAT = "";

        Connection conSPDBA = null;               
        Connection conSPFTA = null;               
        Connection LM_CONDTB = null;

	
        Statement stmSPDBA = null;               
        Statement stmSPDBQ = null;               
        Statement stmSPFTA = null;               
        Statement stmSPFTQ = null;               
	
	// Statements for query
	 
        String M_SPDPA = "f:\\data\\foxdat\\0102\\marksys";
        String M_SPDNA = "camsdata";
        //String M_SPDTA = m_spllibr;
        String M_SPDTA = "spldata";
        String M_SPUSA = "FIMS";
        String M_SPPWA = "FIMS";
        String M_WRKDR = "";
        String M_JARDR = "";
        String M_SPTSYSLC = "";
        String M_SPTSYSLC0 = "";
        String M_DATFLD = "";

        String M_SPCTA = "DTB";
        String M_SPCJA = "|";  // Join character


        String strMSG;
        String LM_FILENM,LM_LIBRNM,LM_DEFDT;
        String LM_MKTTP = "01";
        String LM_LADNO, LM_INVNO, LM_INVDT, LM_CNTDS, LM_LR_NO, LM_LR_DT, LM_PMDDT, LM_INVVL, LM_STSFL, LM_TSLNO;
        String LM_PRDCD, LM_ASSVL, LM_EXCVL, LM_NETVL, LM_INVRT, LM_INVQT, LM_INVPK;
        int cmpINVDT;
        boolean INVDT_FL;
        String LM_INSSTR1, LM_INSSTR2;


        String[][] arrMNTH = {{"01","31"},{"02","28"},{"03","31"},
                                          {"04","30"},{"05","31"},{"06","30"},
                                          {"07","31"},{"08","31"},{"09","30"},
                                          {"10","31"},{"11","30"},{"12","31"},
						 };

	static String[] arrDAYS = {"31","28","31","30","31","30","31","31","30","31","30","31"};



        support()
        {
                try
                {
                        pnlMAIN = new JPanel();
                        pnlMAIN.setLayout(null);
                        LM_CURVL = 20;          LM_SROW  = 200;   LM_SCOL = 200;
                        setNXTVL(80,0);         txtLIBRNM = crtTXT(pnlMAIN,LM_SCOL,LM_SROW,LM_CURVL,20);
                        setNXTVL(80,50);        txtFILENM = crtTXT(pnlMAIN,LM_SCOL,LM_SROW,LM_CURVL,20);
                        txtLIBRNM.setText(M_SPDTA);

                        LM_CURVL = 20;          LM_SROW  += 40;   LM_SCOL = 200;
                        setNXTVL(200,0);         cmbSELOPT = crtCMB(pnlMAIN,"Select",LM_SCOL,LM_SROW,LM_CURVL,20);


                        LM_CURVL = 20;          LM_SROW  += 40;   LM_SCOL = 200;
                        setNXTVL(60,0);         btnRUN1   = crtBTN(pnlMAIN,"RUN",LM_SCOL,LM_SROW,LM_CURVL,20);
                        setNXTVL(60,100);       btnEXIT1  = crtBTN(pnlMAIN,"EXIT",LM_SCOL,LM_SROW,LM_CURVL,20);

                        LM_CURVL = 20;          LM_SROW = 400-20;        LM_SCOL = 200;
                        setNXTVL(200,0);        txtMSG = crtTXT(pnlMAIN,LM_SCOL,LM_SROW,LM_CURVL,20);
                        cmbSELOPT.addItem(strOPT_REWORK_INVQT);
                        cmbSELOPT.addItem(strOPT_CRTTRG_INVQT);


                        pnlMAIN.validate();
                        getContentPane().add(pnlMAIN);
                        exeENBSCR();
                        addLSTNR();
                        exeINTVAR();
                }
                catch(Exception L_EX)
                {
                        System.out.println(L_EX.toString());
		}	
        }


	   public static void main(String args[])
       {           //static  {String m_spllibr = args[0];}
                   support f = new support();
                   f.setSize(800,600);
		   f.show();
	   }
		
		private void exeINTVAR()
        {
                LM_FILENM = "";
                LM_LIBRNM = "";
        }


	private void setNXTVL(int LP_CURVL,int LP_COLGP) {
		LM_PRVVL = LM_CURVL;
		LM_CURVL = LP_CURVL;
		LM_SCOL += LM_PRVVL + LP_COLGP;
                System.out.println(""+LM_SCOL);
	}


        private void exeDSBSCR()  // disable all text fields
        { 
                txtLIBRNM.setEnabled(false);
                txtFILENM.setEnabled(false);
        }


        private void exeENBSCR()  // Enable all text fields
        { 
                txtLIBRNM.setEnabled(true);
                txtFILENM.setEnabled(true);
        }


        private void addLSTNR()
        {
                btnRUN1.addKeyListener(this);
                btnRUN1.addFocusListener(this);
                btnRUN1.setActionCommand("btnRUN1");
                btnEXIT1.addKeyListener(this);
                btnEXIT1.addFocusListener(this);
                btnEXIT1.setActionCommand("btnEXIT1");
                cmbSELOPT.addActionListener(this);
                cmbSELOPT.addItemListener(this);            
                cmbSELOPT.addFocusListener(this);
                cmbSELOPT.addKeyListener(this);
        }



	public void itemStateChanged(ItemEvent L_IE)
        {
                if(((JComboBox)L_IE.getSource()).equals(cmbSELOPT))
                {
                       LM_OPTVL = (String)cmbSELOPT.getSelectedItem();
		}
			
	}


        public void exeREWORK(String LP_strREWORK)
        {
		try
                {
                  int LM_INVCT;
                  String LM_strWHRSTR;
                  if(LP_strREWORK.equals("INVQT"))
                  {
                        LM_strWHRSTR = " where int_indno in (select ivt_indno from mr_ivtrn)";
                        M_strSQLQRY  = "select count(*) ivt_invct from mr_intrn "+LM_strWHRSTR;
                        M_rstRSLSET = exeSQLQRY(M_strSQLQRY,"ACT");
                        if (!M_rstRSLSET.next())
                        {
                                LM_OPTNPN.showMessageDialog(this," No records found in MR_INTRN","Message",JOptionPane.INFORMATION_MESSAGE);
                                return;
                        }
                        LM_INVCT = Integer.parseInt(M_rstRSLSET.getString("IVT_INVCT").toString().trim());
                        M_rstRSLSET.close();

                        M_strSQLQRY  = "update mr_intrn set int_invqt=0, int_trnfl='0' "+LM_strWHRSTR;
                        exeSQLUPD(M_strSQLQRY);
                        M_strSQLQRY  = "select ivt_mkttp,ivt_indno,ivt_prdcd,ivt_pkgtp,sum(ivt_invqt) ivt_invqt from mr_ivtrn ";
                        M_strSQLQRY += " where ivt_indno in (select int_indno from mr_intrn) group by ivt_mkttp,ivt_indno,ivt_prdcd,ivt_pkgtp";
                        M_rstRSLSET = exeSQLQRY(M_strSQLQRY,"ACT");
                        while(true)
                        {
                                if (!M_rstRSLSET.next())
                                {
                                        break;
                                }
                                M_strSQLQRY  = "update MR_INTRN set int_invqt = int_invqt + "+M_rstRSLSET.getString("IVT_INVQT").toString().trim()+", int_trnfl='0' where ";
                                M_strSQLQRY += " int_mkttp = '"+M_rstRSLSET.getString("IVT_MKTTP").toString().trim()+"' and ";
                                M_strSQLQRY += " int_indno = '"+M_rstRSLSET.getString("IVT_INDNO").toString().trim()+"' and ";
                                M_strSQLQRY += " int_prdcd = '"+M_rstRSLSET.getString("IVT_PRDCD").toString().trim()+"' and ";
                                M_strSQLQRY += " int_pkgtp = '"+M_rstRSLSET.getString("IVT_PKGTP").toString().trim()+"'";
                                exeSQLUPD(M_strSQLQRY);
                                setMSG("MR_INTRN : "+M_rstRSLSET.getString("IVT_INDNO").toString().trim()+"   "+LM_INVCT,'N');
                                LM_INVCT = LM_INVCT-1;
                        }
                        M_rstRSLSET.close();

                        LM_strWHRSTR = " where dot_dorno in (select ivt_dorno from mr_ivtrn)";
                        M_strSQLQRY  = "select count(*) ivt_invct from mr_dotrn "+LM_strWHRSTR;
                        M_rstRSLSET = exeSQLQRY(M_strSQLQRY,"ACT");
                        if (!M_rstRSLSET.next())
                        {
                                LM_OPTNPN.showMessageDialog(this," No records found in MR_DOTRN","Message",JOptionPane.INFORMATION_MESSAGE);
                                return;
                        }
                        LM_INVCT = Integer.parseInt(M_rstRSLSET.getString("IVT_INVCT").toString().trim());
                        M_rstRSLSET.close();

                        M_strSQLQRY  = "update mr_dotrn set dot_invqt=0, dot_trnfl='0'"+LM_strWHRSTR;
                        exeSQLUPD(M_strSQLQRY);

                        M_strSQLQRY  = "select ivt_mkttp,ivt_dorno,ivt_prdcd,ivt_pkgtp,sum(ivt_invqt) ivt_invqt from mr_ivtrn ";
                        M_strSQLQRY += " where ivt_dorno in (select dot_dorno from mr_dotrn) group by ivt_mkttp,ivt_dorno,ivt_prdcd,ivt_pkgtp";
                        M_rstRSLSET = exeSQLQRY(M_strSQLQRY,"ACT");
                        while(true)
                        {
                                if (!M_rstRSLSET.next())
                                {
                                        break;
                                }
                                M_strSQLQRY  = "update MR_DOTRN set dot_invqt = dot_invqt + "+M_rstRSLSET.getString("IVT_INVQT").toString().trim()+", dot_trnfl='0' where ";
                                M_strSQLQRY += " dot_mkttp = '"+M_rstRSLSET.getString("IVT_MKTTP").toString().trim()+"' and ";
                                M_strSQLQRY += " dot_dorno = '"+M_rstRSLSET.getString("IVT_DORNO").toString().trim()+"' and ";
                                M_strSQLQRY += " dot_prdcd = '"+M_rstRSLSET.getString("IVT_PRDCD").toString().trim()+"' and ";
                                M_strSQLQRY += " dot_pkgtp = '"+M_rstRSLSET.getString("IVT_PKGTP").toString().trim()+"'";
                                exeSQLUPD(M_strSQLQRY);
                                setMSG("MR_DOTRN : "+M_rstRSLSET.getString("IVT_DORNO").toString().trim()+"    "+LM_INVCT,'N');
                                LM_INVCT = LM_INVCT-1;
                        }
                        M_rstRSLSET.close();
                        LM_OPTNPN.showMessageDialog(this," Reworking Completed","Message",JOptionPane.INFORMATION_MESSAGE);
                  }
                }
		catch (Exception L_EX)
                {
			System.out.println("Exception : "+L_EX);
                }
        }


        public void crtTRIGGR(String LP_strTRGNM)
        {
                if(LP_strTRGNM.equals("updINVQT"))
                {
                        M_strSQLQRY  = "drop trigger "+LP_strTRGNM;
                        exeSQLUPD(M_strSQLQRY);
                        M_strSQLQRY  = " CREATE TRIGGER "+LP_strTRGNM;
                        M_strSQLQRY += " AFTER  UPDATE OF ivt_invqt ON mr_ivtrn ";
                        M_strSQLQRY += " REFERENCING NEW AS new_row OLD AS old_row ";
                        M_strSQLQRY += " FOR EACH ROW MODE DB2ROW ";
                        M_strSQLQRY += " begin";
                        M_strSQLQRY += " UPDATE mr_dotrn ";                    
                        M_strSQLQRY += " SET dot_trnfl='0', dot_invqt =  ";
                        M_strSQLQRY += " isnull(dot_invqt,0)+ ";
                        M_strSQLQRY += " (isnull(new_row.ivt_invqt,0) ";
                        M_strSQLQRY += " -isnull(old_row.ivt_invqt,0)) ";                 
                        M_strSQLQRY += " where dot_mkttp = new_row.ivt_mkttp ";
                        M_strSQLQRY += " and dot_dorno = new_row.ivt_dorno ";
                        M_strSQLQRY += " and dot_prdcd = new_row.ivt_prdcd ";            
                        M_strSQLQRY += " and dot_pkgtp = new_row.ivt_pkgtp ;";
                        M_strSQLQRY += " UPDATE mr_intrn ";                    
                        M_strSQLQRY += " SET int_trnfl='0', int_invqt =  ";
                        M_strSQLQRY += " isnull(int_invqt,0)+ ";
                        M_strSQLQRY += " (isnull(new_row.ivt_invqt,0) ";
                        M_strSQLQRY += " -isnull(old_row.ivt_invqt,0)) ";                 
                        M_strSQLQRY += " where int_mkttp = new_row.ivt_mkttp ";
                        M_strSQLQRY += " and int_indno = new_row.ivt_indno ";
                        M_strSQLQRY += " and int_prdcd = new_row.ivt_prdcd ";            
                        M_strSQLQRY += " and int_pkgtp = new_row.ivt_pkgtp;";
                        ////
						M_strSQLQRY += " UPDATE mr_wbtrn ";                    
                        M_strSQLQRY += " SET wb_trnfl='0', wb_chlqt =  ";
                        M_strSQLQRY += " isnull(wb_chlqt,0)+ ";
                        M_strSQLQRY += " (isnull(new_row.ivt_invqt,0) ";
                        M_strSQLQRY += " -isnull(old_row.ivt_invqt,0)) ";                 
                        M_strSQLQRY += " where wb_doctp ='03' and wb_docno = new_row.ivt_ginno ";
                       ////
						M_strSQLQRY += " end";
                        exeSQLUPD(M_strSQLQRY);
                        LM_OPTNPN.showMessageDialog(this,LP_strTRGNM +" Trigger Created","Message",JOptionPane.INFORMATION_MESSAGE);
                }

        }



	public void actionPerformed(ActionEvent L_AE)
                {
                        if(L_AE.getSource().equals(btnRUN1))
                        {
                                LM_LIBRNM = txtLIBRNM.getText();
                                LM_FILENM = txtFILENM.getText();

                                setCONMKT(LM_LIBRNM.trim());
                               if(LM_OPTVL.equals(strOPT_CRTTRG_INVQT))
                                        crtTRIGGR("updINVQT");
                               else if(LM_OPTVL.equals(strOPT_REWORK_INVQT))
                                        exeREWORK("INVQT");
                        }
                        if(L_AE.getSource().equals(btnEXIT1))
                        {
                                this.dispose();
                                System.exit(0);
                        }
                }




	public String setFMT(String LP_STR,int LP_LEN){
		BigDecimal L_BIGDEC = new BigDecimal(LP_STR);
		try{
			L_BIGDEC = L_BIGDEC.setScale(LP_LEN,BigDecimal.ROUND_HALF_UP);
		}catch(Exception e){
			System.out.println("Error in setFMT : " + e.toString());
		}
		return L_BIGDEC.toString();
	}


		
    public void textValueChanged(TextEvent L_TE){}
	
    public void keyTyped(KeyEvent L_KTE){}

    public void keyPressed(KeyEvent L_KPE) {}


    public void focusLost(FocusEvent L_FE)  {}

    public void focusGained(FocusEvent L_FE){}
	
	
    public void keyReleased(KeyEvent L_KRE){}
			
        private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP)
        {
                LM_RETVL = "";
		try
                {
                        if (LP_FLDTP.equals("C"))
                        {
                                LM_RETVL = nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"");
                        }
                        else if (LP_FLDTP.equals("D"))
                        {
                                LM_RETVL = setDATE("DMY",LP_RSLSET.getDate(LP_FLDNM));
                        }
                        else if (LP_FLDTP.equals("T"))
                        {
                                LM_RETVL = setDTMFMT(LP_RSLSET.getString(LP_FLDNM));
                        }
                 }
		catch (SQLException L_EX)
                {
                        showEXMSG(L_EX,"getRSTVAL");
                }
        return LM_RETVL;
        }
	


        private String getRSTVAL1(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP)
        {
                LM_RETVL = "";
		try
                {
                        if (LP_FLDTP.equals("C"))
                        {
                                LM_RETVL = nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"");
                        }
                        else if (LP_FLDTP.equals("D"))
                        {
                                LM_RETVL = setDATE("DMY",LP_RSLSET.getDate(LP_FLDNM));
                        }
                        else if (LP_FLDTP.equals("T"))
                        {
                                LM_RETVL = setDTMFMT(LP_RSLSET.getString(LP_FLDNM));
                        }
                        else if (LP_FLDTP.equals("D1"))
                        {
                                LM_RETVL = nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"");
                                if(LM_RETVL.length()==8)
                                {
                                        LM_RETVL = LM_RETVL.substring(6,8)+"/"+LM_RETVL.substring(4,6)+"/"+LM_RETVL.substring(0,4);
                                }
                        }
                 }
		catch (SQLException L_EX)
                {
                        showEXMSG(L_EX,"getRSTVAL");
                }
        return LM_RETVL;
        }



public  void exeSQLUPD(String LP_SQLVAL)
{
		try
                {
                        M_intUPDCT = stmSPDBA.executeUpdate(LP_SQLVAL);
                        conSPDBA.commit();
                        System.out.println(M_intUPDCT + " Rows Updated");
		}
                catch(Exception L_SE)
                {
			System.out.println("Error in exeSQLUPD : " + L_SE.toString());
			System.out.println("Error Query : " + LP_SQLVAL);
		}
}


        public  Connection setCONDTB(String LP_DTBLB, String LP_DTBUS, String LP_DTBPW)
        {
		try
                {       String L_STRURL = "", L_STRDRV = "";
                        L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
                        L_STRURL = "jdbc:as400://SPLWS02/";
                        Class.forName(L_STRDRV);

                        L_STRURL = L_STRURL + LP_DTBLB;
                        LM_CONDTB = DriverManager.getConnection(L_STRURL,LP_DTBUS,LP_DTBPW);
                        System.out.println(LM_CONDTB);

                        if(LM_CONDTB == null)
				return null;
                        LM_CONDTB.setAutoCommit(false);

                        SQLWarning L_STRWRN = LM_CONDTB.getWarnings();
			if ( L_STRWRN != null )
			   System.out.println("Warning in setCONDTB : "+L_STRWRN);
                        return LM_CONDTB;
		}
                catch(java.lang.Exception L_EX)
                {
			System.out.println("setCONDTB" + L_EX.toString());
		}
		return null;
    }



        public void setCONMKT(String LP_LIBRNM)
        {
                try
                {
                        //conSPFTA = setCONFTB(M_SPDPA);
                        conSPDBA = setCONDTB(LP_LIBRNM,M_SPUSA,M_SPPWA);
                        if(conSPDBA != null)
                        {
                                conSPDBA.rollback();
                                stmSPDBA = chkMKTSTM(conSPDBA);
                                stmSPDBQ = chkMKTSTM(conSPDBA);
                                System.out.println("Connection "+conSPDBA+" Establshed with "+LP_LIBRNM);
                        }
                }
                catch(Exception L_EX)
                {
                        //showEXMSG(L_EX,"setCONMKT");
                }
	}



	public JButton crtBTN(JPanel LP_PANEL,String LP_LABEL,int LP_XPOS,int LP_YPOS,int LP_WIDTH,int LP_HEIGHT)
        {
        JButton btnNEW = new JButton(LP_LABEL); 
		btnNEW.setSize(LP_WIDTH,LP_HEIGHT);
		btnNEW.setLocation(LP_XPOS,LP_YPOS);
		btnNEW.addActionListener(this);
        LP_PANEL.add(btnNEW);
		return btnNEW; 
	}



	public JTextField crtTXT(JPanel LP_PANEL,int LP_XPOS,int LP_YPOS,int LP_WIDTH,int LP_HEIGHT)
        {
    	JTextField txtNEW = new JTextField();
		txtNEW.setLocation(LP_XPOS,LP_YPOS);
		txtNEW.setSize(LP_WIDTH,LP_HEIGHT);
                //txtNEW.setHorizontalAlignment(SwingConstant.LEFT);
                LP_PANEL.add(txtNEW);
                System.out.println(""+LP_XPOS+" "+LP_YPOS+" "+LP_WIDTH+" "+LP_HEIGHT);
		return txtNEW; 
	}
	
	// Returns new JLabel
	public JLabel crtLBL(JPanel LP_PANEL,String LP_LABEL,int LP_XPOS,int LP_YPOS,int LP_WIDTH,int LP_HEIGHT)
        {
   		JLabel lblNEW = new JLabel(LP_LABEL);
		lblNEW.setLocation(LP_XPOS,LP_YPOS);
		lblNEW.setSize(LP_WIDTH,LP_HEIGHT);
                LP_PANEL.add(lblNEW);
		return lblNEW;
	}
	
	// Returns new JComboBox
    public JComboBox crtCMB(JPanel LP_PANEL,String LP_ITEM,int LP_XPOS,int LP_YPOS,int LP_WIDTH,int LP_HEIGHT)
    {
    	JComboBox cmbNEW = new JComboBox();
		cmbNEW.addItem(LP_ITEM);
		cmbNEW.setLocation(LP_XPOS,LP_YPOS);
		cmbNEW.setSize(LP_WIDTH,LP_HEIGHT);
		LP_PANEL.add(cmbNEW);
		return cmbNEW; 
    }
	
	// Returns new JCheckBox
	public JCheckBox crtCHKBOX(JPanel LP_PANEL,String LP_LABEL,int LP_XPOS,int LP_YPOS,int LP_WIDTH,int LP_HEIGHT,boolean LP_CHKFLG)
        {
		JCheckBox chkNEW = new JCheckBox(LP_LABEL,LP_CHKFLG);
		chkNEW.setSize(LP_WIDTH,LP_HEIGHT);
		chkNEW.setLocation(LP_XPOS,LP_YPOS);
		LP_PANEL.add(chkNEW);
		return chkNEW;
	}
	
	// Returns new JRadioButton added to given Button group
	public JRadioButton crtRDOGRP(JPanel LP_PANEL,ButtonGroup LP_RDOGRP,String LP_CHKITM,int LP_XPOS,int LP_YPOS,int LP_WIDTH,int LP_HEIGHT,boolean LP_CHKFLG)
        {
         JRadioButton  rdbNEW = new JRadioButton(LP_CHKITM,LP_CHKFLG); 
		 LP_RDOGRP.add(rdbNEW);
		 rdbNEW.setSize(LP_WIDTH,LP_HEIGHT);
		 rdbNEW.setLocation (LP_XPOS,LP_YPOS);
		 LP_PANEL.add(rdbNEW);
         return rdbNEW;
        }



        public Statement chkMKTSTM(Connection LP_CONVAL)
        {
		try{
			if (LP_CONVAL != null)
                        {
                           return LP_CONVAL.createStatement();
			}
		}catch(Exception L_EX){}
		return null;			
	}




        public  String getCURTIM()
        {
		String L_STRQRY;
		ResultSet L_RSLSET;
	
		String L_CURTM = "";
		try {
		L_STRQRY = "select CONVERT (TIME, CURRENT_TIMESTAMP) SP_CURTM from CO_SPTRN";
                L_RSLSET = exeSQLQRY(L_STRQRY,"ACT");
		if (L_RSLSET.next()) {
			L_CURTM=L_RSLSET.getString("SP_CURTM").toString().trim().substring(0,5);
			L_RSLSET.close();
                  } // if (M_rstRSLSET.next())
		} // try
		catch (Exception L_EX){
			System.out.println("Exception : "+L_EX);
		 } // catch (Exception L_EX){
		return L_CURTM;
	}
												    
	// Method to get the current date in dd/MM/yyyy (Added by Santosh on 14/08/2002)
        public  String getCURDTM()
        {
        String strCURDT;
        java.util.Date oCURDT = new java.util.Date();
		SimpleDateFormat dtFORM = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat tmFORM = new SimpleDateFormat("HH:mm");
		
		//strCURDT = dtFORM.format(oCURDT);			// Changed on 03/12/2002
                strCURDT = M_LOGDAT;
		strCURDT += " ";
		strCURDT += getCURTIM();
		
		if(strCURDT.length() > 14){
			if(strCURDT.substring(11,13).equals("24"))
				strCURDT = strCURDT.substring(0,11) + "23:59";
			else if((strCURDT.substring(11,13).equals("00"))&& (strCURDT.substring(14).equals("00")))
				strCURDT = strCURDT.substring(0,11) + "00:01";
		}
		
		return strCURDT;
	}
	
        public  String setDATE(String LP_DATFMT,java.util.Date oDT)
        {
		String strCURDT = "";
        SimpleDateFormat dtFORM;
        if (LP_DATFMT.equals("DMY"))
           dtFORM = new SimpleDateFormat("dd/MM/yyyy");
        else 
           dtFORM = new SimpleDateFormat("MM/dd/yyyy");
		if(oDT != null)
			strCURDT = dtFORM.format(oDT);
		return strCURDT;
	}
	
        public  String setDATFMT(String LP_FMTTP,String LP_DATSTR)
        {
                LM_RTNSTR = "";
		try{
			if(LP_FMTTP.equals("MDY")){
				if(LP_DATSTR != null){
					if(LP_DATSTR.trim().length() == 10)
                                                LM_RTNSTR = LP_DATSTR.substring(3,5)+ "/"+ LP_DATSTR.substring(0,2)+"/"+LP_DATSTR.substring(6,10);
				}
			}
			else if(LP_FMTTP.equals("DMY")){
				if(LP_DATSTR != null){
					if(LP_DATSTR.trim().length() == 8)
                                                LM_RTNSTR = LP_DATSTR.substring(0,2)+ "/"+LP_DATSTR.substring(3,5)+"/"+"20"+LP_DATSTR.substring(6,8);
				}
			}
		}catch(Exception L_EX){
			System.out.println("setDATFMT: "+L_EX);
		}
                return LM_RTNSTR;
	}
	
        public   String setDTMFMT(String LP_DTMSTR)
        {
                LM_RTNSTR = "";
		try{
			if(LP_DTMSTR != null){
				if(LP_DTMSTR.length() > 0){
					int L_LEN = LP_DTMSTR.trim().length();
					if(L_LEN >=16){
                                                LM_RTNSTR = LP_DTMSTR.substring(8,10)+"/"+ LP_DTMSTR.substring(5,7)+"/"+LP_DTMSTR.substring(0,4);
                                            LM_RTNSTR = LM_RTNSTR + " "+ LP_DTMSTR.substring(11,13)+ ":" + LP_DTMSTR.substring(14,16);
					}
                                        if(LM_RTNSTR.trim().equals("30/12/1899 00:00"))
                                           LM_RTNSTR = "";
				}
			}
		}catch(Exception L_EX){
			System.out.println("setDTMFMT: "+L_EX);
		}
                return LM_RTNSTR;
	}
	
	
        public  String getSTRYR(String L_CURDT)
        {
		String L_STRDT= "";
		try{
			if(L_CURDT.length() == 10){
				String L_CURMT = L_CURDT.substring(3,5).toString().trim();
				String L_CURYR = L_CURDT.substring(6,10).toString().trim();
				int L_MONTH = Integer.parseInt(L_CURMT);
				int L_YEAR = Integer.parseInt(L_CURYR);
				if((L_MONTH > 0) && (L_MONTH < 7)){
					int L_STRYR = L_YEAR - 1;
					L_STRDT = "01/04/" + String.valueOf(L_STRYR).trim();
				}else
					L_STRDT = "01/04/" + L_CURYR;
			}
		}catch(Exception L_EX){
			System.out.println("getSTRYR: "+L_EX);				
		}
		return L_STRDT;
	}

        public  String getABDATE(String LP_STRDT,int LP_DDCNT,char LP_ABFLG)
        {
		// remark Max limit for adding or removing days = 28
		
		//String L_DDVAL;
		int L_YRVAL = Integer.parseInt(LP_STRDT.substring(6,10));
		if(L_YRVAL%4 == 0)
			arrDAYS[1] = "29";
		else
			arrDAYS[1] = "28";
		if(LP_STRDT.length()>=10){
			//L_DDVAL = LP_STRDT.substring(0,2);
			// L_MMVAL = LP_STRDT.substring(3,2);
			int L_DDVAL = Integer.parseInt(LP_STRDT.substring(0,2));
			int L_MMVAL = Integer.parseInt(LP_STRDT.substring(3,5));
			L_YRVAL = Integer.parseInt(LP_STRDT.substring(6,10));
			if(LP_ABFLG == 'A'){
				L_DDVAL += LP_DDCNT;
				if(L_DDVAL > Integer.parseInt(arrDAYS[L_MMVAL -1])){
					if(L_MMVAL != 12){
						L_DDVAL = L_DDVAL - Integer.parseInt(arrDAYS[L_MMVAL -1]);
						L_MMVAL += 1; 
					}
					else{
						L_DDVAL = L_DDVAL - Integer.parseInt(arrDAYS[L_MMVAL -1]); 
						L_MMVAL = 1; 
						L_YRVAL +=1;  
					}
				}
			}else if(LP_ABFLG == 'B'){
				L_DDVAL -= LP_DDCNT;
				if(L_DDVAL <=0){
					if(L_MMVAL!= 1)
						L_MMVAL -= 1; 
					else{
						L_MMVAL = 12;
						L_YRVAL -= 1; 
					}
					L_DDVAL = L_DDVAL + Integer.parseInt(arrDAYS[L_MMVAL -1]);
				}
			}
			String L_STRDD = L_DDVAL + " ";
			if(L_STRDD.trim().length() == 1)
				L_STRDD = "0"+L_STRDD.trim();
		  
			String L_STRMM = L_MMVAL + " ";
			if(L_STRMM.trim().length() == 1)
				L_STRMM = "0"+L_STRMM.trim();
		  
			String L_STRRTN = L_STRDD.trim() + "/" + L_STRMM.trim() + "/" + L_YRVAL;
			return L_STRRTN;
		}
		else 
			return "";
	}
	
        public  boolean exeVLDDT(String LP_STRDT)
        {
		// String is in the form dd/mm/yyyy
		try{
			if(LP_STRDT.length() != 10)
				return false;
			else if(LP_STRDT.length() == 10){
				int L_YRVAL = Integer.parseInt(LP_STRDT.substring(6,10));
				if(L_YRVAL%4 == 0)
					arrMNTH[1][1] = "29";
				else
					arrMNTH[1][1] = "28";
				for(int i=0;i<12;i++){
					if(LP_STRDT.substring(3,5).equals(arrMNTH[i][0])){
						int L_VAL = arrMNTH[i][1].compareTo(LP_STRDT.substring(0,2));
						if(L_VAL >=0)
						   return true;
						else 
							return false;
					}
				}
			}
		}catch(Exception L_EX){
			System.out.println("exeVLDDT: "+L_EX);				
		}
		return false;
	}
	
        public  String exeVLDDT1(String LP_STRDT)
        {        // Added by Santosh on 17/07/02
		String LP_RSTRDT = "";
		try{
			if(LP_STRDT.length() == 10){
				int L_YRVAL = Integer.parseInt(LP_STRDT.substring(6,10));
				if(L_YRVAL%4 == 0)
					arrMNTH[1][1] = "29";
				else
					arrMNTH[1][1] = "28";
				for(int i=0;i<12;i++){
					if(LP_STRDT.substring(3,5).equals(arrMNTH[i][0])){
						int L_VAL = arrMNTH[i][1].compareTo(LP_STRDT.substring(0,2));
						if(L_VAL >=0){
                                                        if(cmpDATE(LP_STRDT.trim(),M_LOGDAT.trim()) < 1){
								LP_STRDT = LP_STRDT.replace(LP_STRDT.charAt(2),'/');
								LP_STRDT = LP_STRDT.replace(LP_STRDT.charAt(5),'/');
								LP_RSTRDT = LP_STRDT;
								return LP_RSTRDT;
							}
						}
					}
				}
			}
			return LP_RSTRDT;
		}catch(Exception L_EX){
			System.out.println("exeVLDDT: "+L_EX);				
			return LP_RSTRDT;
		}
	}
	
        public  boolean exeVLDDTM(String LP_STRDTM)
        {
		String L_STRDT, L_STRTM;
		if(LP_STRDTM.trim().length() == 16){
			L_STRDT = LP_STRDTM.substring(0,10);
			L_STRTM = LP_STRDTM.substring(11,16);
			if(exeVLDDT(L_STRDT))
                if(exeVLDTM(L_STRTM))
                  return true;
		}
		return false;
        }
	
        public  boolean exeVLDTM(String LP_STRTM)
        {
	    if(LP_STRTM.trim().length() !=5)  // HH:mm
			return false;
		char[] arrTMCHR = LP_STRTM.toCharArray();
		for(int i=0;i<arrTMCHR.length;i++){
			if(Character.isDigit(arrTMCHR[i]))
				if(i==2)
					return false;
            else if(arrTMCHR[2] != ':')
				if(arrTMCHR[2] != '.')
					return false;
            else if((Character.isLetter(arrTMCHR[i]))||(Character.isWhitespace(arrTMCHR[i]))||(Character.isWhitespace(arrTMCHR[i])))
				return false;
		}
        int L_HRVAL = Integer.parseInt(LP_STRTM.substring(0,2));
        int L_MIVAL = Integer.parseInt(LP_STRTM.substring(3,5));
        if(L_HRVAL > 24)
			return false;
        else if (L_MIVAL > 60)
            return false;
        else
            return true;
	}
	
	
	// Return parameter		0: Dates equal
	//						1: 1st greater
	//					   -1: 1st smaller
        public  int cmpDATTM(String LP_DTMPAR1,String LP_DTMPAR2)
        {
		if(LP_DTMPAR1.length()<16)
			return 2; // Error code
		else if(LP_DTMPAR2.length()<16)
			return 2; // Error code
		int L_RSLT = cmpDATE(LP_DTMPAR1.substring(0,10).trim(),LP_DTMPAR2.substring(0,10).trim()) ;
		
		if(L_RSLT != 0)
			return L_RSLT;
		if(LP_DTMPAR1.substring(11,13).compareTo(LP_DTMPAR2.substring(11,13))== 0){
			// Hours equal
			if(LP_DTMPAR1.substring(14,16).compareTo(LP_DTMPAR2.substring(14,16))== 0)
				return 0;
			else if(LP_DTMPAR1.substring(14,16).compareTo(LP_DTMPAR2.substring(14,16))< 0)
				return -1;
			else if(LP_DTMPAR1.substring(14,16).compareTo(LP_DTMPAR2.substring(14,16))> 0)
				return 1;
		}
		else if(LP_DTMPAR1.substring(11,13).compareTo(LP_DTMPAR2.substring(11,13))< 0)
			return -1;
		else if(LP_DTMPAR1.substring(11,13).compareTo(LP_DTMPAR2.substring(11,13))> 0)
			return 1;
		return 0;
	}
	
        public  int cmpDATE(String LP_DATPAR1,String LP_DATPAR2)
        {
                LM_RTNSTR = "";
		// Return parameter 
		
		// 0: Dates equal
		// 1: Ist greater
		// -1" Ist smaller
		
		if(LP_DATPAR1.trim().length() != 10 || LP_DATPAR2.trim().length() != 10)
			return 2; // error code 
		
		if(LP_DATPAR1.substring(6,10).compareTo(LP_DATPAR2.substring(6,10))== 0){ // Year Equal
			if(LP_DATPAR1.substring(3,5).compareTo(LP_DATPAR2.substring(3,5))== 0){ // Month Equal
				if(LP_DATPAR1.substring(0,2).compareTo(LP_DATPAR2.substring(0,2))== 0)
					return 0;
				else if(LP_DATPAR1.substring(0,2).compareTo(LP_DATPAR2.substring(0,2))< 0)
					return -1;
				else if(LP_DATPAR1.substring(0,2).compareTo(LP_DATPAR2.substring(0,2))> 0)
					return 1;
			}
			else if(LP_DATPAR1.substring(3,5).compareTo(LP_DATPAR2.substring(3,5))< 0)
				return -1;
			else if(LP_DATPAR1.substring(3,5).compareTo(LP_DATPAR2.substring(3,5))> 0)
				return 1;
		}
		else if(LP_DATPAR1.substring(6,10).compareTo(LP_DATPAR2.substring(6,10))< 0)
			return -1;
		else if(LP_DATPAR1.substring(6,10).compareTo(LP_DATPAR2.substring(6,10))> 0)
			return 1;
		return 0;
	}
	
        public  String setDBSDT(String LP_STRDAT)
        {
                LM_RTNSTR = "null";
		try
                {
                        if(LP_STRDAT != null && LP_STRDAT.length() == 10)
                        {
                                LP_STRDAT = setDATFMT("MDY",LP_STRDAT);
                                if(M_SPCTA.equals("DTB"))
                                        LM_RTNSTR = "'" + LP_STRDAT + "'";
                                else if(M_SPCTA.equals("DBS"))
                                        LM_RTNSTR = "ctod('" + LP_STRDAT + "')";
                        }
                        else
                        {
                        if(M_SPCTA.equals("DTB"))
                                LM_RTNSTR = "null";
                        else if(M_SPCTA.equals("DBS"))
                                LM_RTNSTR = "ctod('  /  /    ')";
                        }
		}
                catch(Exception L_EX)
                {
			System.out.println("setDBSDT: "+L_EX);
		}
                return LM_RTNSTR;
	}
	
	// To return the Timestamp in the correct Database Format
        public  String setDBSTM(String LP_DTMSTR)
        {
                LM_RTNSTR = "null";
		try{
                        if(LP_DTMSTR != null && LP_DTMSTR.length() > 10){
                                LM_RTNSTR = setDTMFMT(LP_DTMSTR);
				if(LP_DTMSTR.trim().length() == 16){
					if((LP_DTMSTR.substring(11,13).equals("24")) && (LP_DTMSTR.substring(14,16).equals("00"))){
						LP_DTMSTR = LP_DTMSTR.substring(0,11) + "23" + ":" + "59";
					}else if(LP_DTMSTR.substring(11,13).equals("00")){
						if(LP_DTMSTR.substring(14,16).equals("00"))// minutes
							LP_DTMSTR = LP_DTMSTR.substring(0,11) + "00" + ":" + "01";
					}
                                        if(M_SPCTA.equals("DTB")){
                                                LM_RTNSTR = LP_DTMSTR.substring(6,10) + "-" + LP_DTMSTR.substring(3,5) + "-" + LP_DTMSTR.substring(0,2);
                                                LM_RTNSTR += "-" + LP_DTMSTR.substring(11,13) + "." + LP_DTMSTR.substring(14,16) + "." + "00";
                                                LM_RTNSTR = "'"+LM_RTNSTR+"'";
					}
                                        else if(M_SPCTA.equals("DBS")){
                                                LM_RTNSTR = LP_DTMSTR.substring(3,5) + "/" + LP_DTMSTR.substring(0,2) + "/" + LP_DTMSTR.substring(6,10);
                                                LM_RTNSTR = LM_RTNSTR + " " + LP_DTMSTR.substring(11,13) + ":" + LP_DTMSTR.substring(14,16) + ":00";
                                                LM_RTNSTR = "ctot('"+LM_RTNSTR+"')";
					}
				}
			}else{
                                if(M_SPCTA.equals("DTB"))
                                        LM_RTNSTR = "null";
                                else if(M_SPCTA.equals("DBS"))
                                        LM_RTNSTR = "ctot('  /  /    ')";
			}
		}catch(Exception L_EX){
			System.out.println("setDBSTM: "+L_EX);
		}
                return LM_RTNSTR;
	}
	
	// Method to calculate the difference between two timestamps
	// Assumption : Final Date is not less than Initial Date
	// LP_FINTM  dd/mm/yyyy HH:MM
        public  String calTIME(String LP_FINTM,String LP_INITM)
        {
		String L_DIFTM = "";
		try{
			int L_YRS,L_MTH,L_DAY,L_HRS,L_MIN;
			int L_YRS1,L_MTH1,L_DAY1,L_HRS1,L_MIN1;
			int L_YRS2,L_MTH2,L_DAY2,L_HRS2,L_MIN2;
			String L_HOUR,L_MINUTE;
			
			if(LP_FINTM.equals("") || LP_INITM.equals(""))
				return L_DIFTM;
			
			// Seperating year,month,day,hour & minute from Final time
			L_YRS1 = Integer.parseInt(LP_FINTM.substring(6,10));
			L_MTH1 = Integer.parseInt(LP_FINTM.substring(3,5));
			L_DAY1 = Integer.parseInt(LP_FINTM.substring(0,2));
			L_HRS1 = Integer.parseInt(LP_FINTM.substring(11,13));
			L_MIN1 = Integer.parseInt(LP_FINTM.substring(14));
			
			// Seperating year,month,day,hour & minute from Initial time
			L_YRS2 = Integer.parseInt(LP_INITM.substring(6,10));
			L_MTH2 = Integer.parseInt(LP_INITM.substring(3,5));
			L_DAY2 = Integer.parseInt(LP_INITM.substring(0,2));
			L_HRS2 = Integer.parseInt(LP_INITM.substring(11,13));
			L_MIN2 = Integer.parseInt(LP_INITM.substring(14));
			
			// Checking for leap year
			if(L_YRS1%4 == 0)
				arrDAYS[1] = "29";
			else
				arrDAYS[1] = "28";
			
			L_MIN = L_MIN1 - L_MIN2;
			L_HRS = L_HRS1 - L_HRS2;
			L_DAY = L_DAY1 - L_DAY2;
			
			if(L_MIN < 0){
				L_MIN += 60;
				L_HRS--;
			}
			if(L_HRS < 0){
				L_HRS += 24;
				L_DAY--;
			}
			if(L_DAY > 1)
				L_HRS += L_DAY*24;
			
			L_HOUR = String.valueOf(L_HRS);
			L_MINUTE = String.valueOf(L_MIN);
			if(L_HOUR.length() < 2)
				L_HOUR = "0" + L_HOUR;
			if(L_MINUTE.length() < 2)
				L_MINUTE = "0" + L_MINUTE;
			L_DIFTM = L_HOUR + ":" + L_MINUTE;
		}catch(Exception e){
			System.out.println("Error in calTIME : " + e.toString());
		}
		return L_DIFTM;
	}	

public ResultSet exeSQLQRY(String LP_SQLVAL, String LP_CONTP)
{
        M_rstRSLSET = null;
	try{
		if (LP_CONTP.equals("ACT") && conSPDBA != null)
                        M_rstRSLSET = stmSPDBQ.executeQuery(LP_SQLVAL);
                if (LP_CONTP.equals("REM") && conSPFTA != null)
                        M_rstRSLSET = stmSPFTQ.executeQuery(LP_SQLVAL);
	}catch(Exception L_SE){
		System.out.println("Error in exeSQLQRY : "+L_SE.toString());
                System.out.println("QUERY Failed: "+"/"+LP_CONTP+"/"+LP_SQLVAL);
	  }
        return M_rstRSLSET;
}
  

        public  Statement chkCONSTM1(Connection LP_CONVAL)
        {
                Statement L_stmSPXXA = null;
		try
                {
			if (LP_CONVAL != null)
                        {
                           LP_CONVAL.setAutoCommit(false);
                           L_stmSPXXA = LP_CONVAL.createStatement();
			}
		}
                catch(Exception L_EX){}
                return L_stmSPXXA;             
	}

        public  void showEXMSG(Exception LP_EX, String LP_MTHNM){
		System.out.println(LP_MTHNM+" : "+LP_EX.toString());
                //setMSG(LP_MTHNM+" : "+LP_EX.toString(),'E');
	}
	

    public void setMSG(String LP_STRMSG,char LP_MSGTP)
    {
        // LP_MSGTP: Message Type :Error or Normal(E or N) 
		//	for Error : color code :RED
		//  for Normal : color code : BLue
                txtMSG.setEnabled(true);
		if(LP_MSGTP == 'N')
                        txtMSG.setForeground(Color.blue);
		else if(LP_MSGTP == 'E')
                        txtMSG.setForeground(Color.red);
                txtMSG.setFont(new Font("Times new Roman",Font.BOLD,11));
                txtMSG.setText(LP_STRMSG);
                txtMSG.paintImmediately(txtMSG.getVisibleRect());
	}



	public String nvlSTRVL(String LP_VARVL, String LP_DEFVL)
        {
		try{
                        //System.out.println("LP_VARVL : "+LP_VARVL);
                        if (LP_VARVL != null) {
                           if (LP_VARVL.trim().length() > 0) {
				LP_VARVL = LP_VARVL.trim();
                                } // if (LP_VARVL.trim().length() > 0)
                           else {
				LP_VARVL = LP_DEFVL;
                           } // if (LP_VARVL.trim().length() > 0)
                        } // if (LP_VARVL != null)
                        else {
				LP_VARVL = LP_DEFVL;
                        } // if (LP_VARVL != null) {
                }catch (Exception L_EX){
			showEXMSG(L_EX,"nvl");
		}
		return LP_VARVL;
	}

	public double nvlNUMDB(String LP_VARVL)
        {
		double LP_RETVL = 0;
		try{
                        System.out.println("LP_VARVL: "+LP_VARVL);
			if(LP_VARVL.length() > 0)
				LP_RETVL = Double.parseDouble(LP_VARVL);
		}catch (Exception L_EX){
			showEXMSG(L_EX,"nvlNUMDB");
		}
		return LP_RETVL;
	}

	public int nvlNUMIN(String LP_VARVL)
        {
		int LP_RETVL = 0;
		try{
			if(LP_VARVL != null)
				LP_RETVL = Integer.parseInt(LP_VARVL);
		}catch (Exception L_EX){
			showEXMSG(L_EX,"nvlNUMIN");
		}
		return LP_RETVL;
	}

	public float nvlNUMFL(String LP_VARVL)
        {
		float LP_RETVL = 0;
		try{
			if(LP_VARVL != null)
				LP_RETVL = Float.parseFloat(LP_VARVL);
		}catch (Exception L_EX){
			showEXMSG(L_EX,"nvlNUMFL");
		}
		return LP_RETVL;
	}
}

