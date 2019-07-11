import java.sql.*;import java.util.Hashtable;
import javax.swing.JButton;import javax.swing.JTabbedPane;import javax.swing.JPanel;import javax.swing.JComponent;
import javax.swing.JTextField;import javax.swing.JOptionPane;import javax.swing.JTable;import javax.swing.JComboBox;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.InputVerifier;
class mm_teaut extends cl_pbase implements ChangeListener
{
	//private co_teautTBLINVFR objTBLVRF;
	private JTextField txtTEMP;
	private JButton btnRPDC1,btnRPDC2,btnRPDC3;
	private JTabbedPane jtpTRNDT;
	private JPanel pnlGPAPR,pnlGPAUT,pnlINAUT;
	private cl_JTable tblGPAPR,tblGPAUT,tblINAUT,tblITDT1,tblITDT2,tblITDT3;
    private final int TBL_CHKFL =0;
	private final int TBL_DSPFL =1;
	private final int TBL_STRTP =2;
	private final int TBL_DOCTP =3;
	private final int TBL_DOCNO =4;
	private final int TBL_DPTCD =5;
	private final int TBL_APRVL =6;
	private final int TBL_VENCD =8;
	private final int TBL_APRFL =7;
	private final int TBL_REMDS =9;
	private final int TBL_COMDS =10;
	private final int TBL_PREBY =11;
	private final int TB1_PREBY =10;
	private final int TBL_VEHNO =12;
	private final int TBL_VEHDS =13;
	
	private Hashtable hstCODDS,hstCODCD;
	private boolean flgDATA = false;
	private java.sql.Date datTEMP;
	/** 
	 * For Using Input verifier, for textfield and JTable,register with the input verifier
	 */

	mm_teaut()
	{
	   super(1);	
	   try
	   {
		hstCODDS = new Hashtable();   
		hstCODCD = new Hashtable();   
		setMatrix(20,8);
		jtpTRNDT=new JTabbedPane();
		pnlGPAPR = new JPanel(null);
		pnlGPAUT = new JPanel(null);
		pnlINAUT = new JPanel(null);
	//	String[] L_strTB1HD = {"SEL","DSP","Str.Type ","GP Type","GP NO.","Department","Approx value","Vendor","Remark","Comment","Prep by"};
	//	int[] L_intCOLSZ = {25,30,55,50,65,75,72,60,200,200,10};
		
		String[] L_strTB1HD = {"SEL","DSP","Str.Type ","GP Type","GP NO.","Dept.","Approx value","<HTML> <P COLOUR = blue> Approval Flag </P></HTML>","Vendor","Remark","Comment","Prep by","Veh No.","Veh Desc"};
		int[] L_intCOLSZ = {25,30,55,50,65,55,72,60,50,200,200,5,5,5};
		
		String[] L_strTB2HD = {" ","Item Code ","UOM","Description","GP Qty.","Due Date"};
		int[] L_intCL2SZ = {20,90,60,400,90,90};
		
		String[] L_strTB3HD = {"SEL","DSP","Str.Type ","Type","Indent NO.","Dept.","Approx value","<HTML> <P COLOUR = blue> Approval Flag </P></HTML>","Remark","Comment","Prep by"};
		int[] L_intCO3SZ = {25,30,55,40,60,40,72,90,200,200,10};
		String[] L_strTB4HD = {" ","Item Code ","UOM","Description","Ind Qty.","Req Date","Exp Date","T.C","Ins"};
		int[] L_intCL4SZ = {20,80,40,300,80,80,80,25,25};
		
		tblGPAPR = crtTBLPNL1(pnlGPAPR,L_strTB1HD,100,1,1,5.2,7.9,L_intCOLSZ,new int[]{0,1});
		add(btnRPDC1=new JButton("Show Document"),7,7,1,1.5,pnlGPAPR,'L');
		tblITDT1 = crtTBLPNL1(pnlGPAPR,L_strTB2HD,100,8,1,8.3,7.9,L_intCL2SZ,new int[]{0});
		
		tblGPAUT = crtTBLPNL1(pnlGPAUT,L_strTB1HD,100,1,1,5.2,7.9,L_intCOLSZ,new int[]{0,1});
		add(btnRPDC2=new JButton("Show Document"),7,7,1,1.5,pnlGPAUT,'L');
		tblITDT2 = crtTBLPNL1(pnlGPAUT,L_strTB2HD,100,8,1,8.3,7.9,L_intCL2SZ,new int[]{0});
		
		tblINAUT = crtTBLPNL1(pnlINAUT,L_strTB3HD,100,1,1,5.2,7.9,L_intCO3SZ,new int[]{0,1});
		add(btnRPDC3=new JButton("Show Document"),7,7,1,1.5,pnlINAUT,'L');
		tblITDT3 = crtTBLPNL1(pnlINAUT,L_strTB4HD,100,8,1,8.3,7.9,L_intCL4SZ,new int[]{0,7,8});
		tblINAUT.setColumnColor(TBL_APRFL,java.awt.Color.blue);
		tblGPAPR.setColumnColor(TBL_APRFL,java.awt.Color.blue);
		jtpTRNDT.add(pnlGPAPR,"GP Approval");
	    jtpTRNDT.add(pnlGPAUT,"GP Authorisation");
		jtpTRNDT.add(pnlINAUT,"Indent Authorisation");
		jtpTRNDT.addMouseListener(this);
		tblGPAPR.addMouseListener(this);
		tblGPAUT.addMouseListener(this);
		tblINAUT.addMouseListener(this);
		jtpTRNDT.addChangeListener(this);
		JComboBox cmbAUTST = new JComboBox();
		cmbAUTST.addItem("Select");
		cmbAUTST.addItem("Held For Discussion");
		cmbAUTST.addItem("Authorised");
        
        JComboBox cmbGPAUT = new JComboBox();
		cmbGPAUT.addItem("Select");
		cmbGPAUT.addItem("Held For Discussion");
		cmbGPAUT.addItem("Approved");

		tblINAUT.setCellEditor(TBL_APRFL,cmbAUTST);
		tblGPAPR.setCellEditor(TBL_APRFL,cmbGPAUT);
		add(jtpTRNDT,1,1,18,8,this,'L');
		M_strSQLQRY = "SELECT CMT_CODCD,CMT_SHRDS from CO_CDTRN WHERE CMT_CGMTP||CMT_CGSTP IN('SYSCOXXDPT','SYSMMXXSST','SYSMMXXMGP') AND IFNULL(CMT_STSFL,'') <>'X'"; 
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET !=null)
		{
			while(M_rstRSSET.next())
			{
				hstCODDS.put(M_rstRSSET.getString("CMT_CODCD"),nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""));
				hstCODCD.put(nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""),M_rstRSSET.getString("CMT_CODCD"));
			}
		}
	   }
	   catch(Exception L_E)
	   {
		   setMSG(L_E,"Constructor");
	   }
	}
	public void stateChanged(ChangeEvent L_CE)
	{
		int L_intSELCT =0;
		setMSG("",'N');
		if(jtpTRNDT.getSelectedIndex()==0)
		{
			L_intSELCT =0;
			for(int i=0;i<tblGPAUT.getRowCount();i++)
			{
				if(tblGPAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT > 0)
			{
				setMSG("All selections in Gate Pass Authorisation will be lost..",'E');
			}
			L_intSELCT =0;
			for(int i=0;i<tblINAUT.getRowCount();i++)
			{
				if(tblINAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT > 0)
			{
				setMSG("All selections in Indent Authorisation will be lost..",'E');
			}
		}
		else if(jtpTRNDT.getSelectedIndex()==1)
		{
			for(int i=0;i<tblGPAPR.getRowCount();i++)
			{
				if(tblGPAPR.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT >0)
			{
				setMSG("All selections for Gate Pass Approval will be lost..",'E');
			}
			L_intSELCT =0;
			for(int i=0;i<tblINAUT.getRowCount();i++)
			{
				if(tblINAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT > 0)
			{
				setMSG("All selections in Indent Authorisation will be lost..",'E');
			}
		}
		else if(jtpTRNDT.getSelectedIndex()==2)
		{
			for(int i=0;i<tblGPAPR.getRowCount();i++)
			{
				if(tblGPAPR.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT >0)
			{
				setMSG("All selections in Gate Pass Approval will be lost..",'E');
			}
			L_intSELCT =0;
			for(int i=0;i<tblGPAUT.getRowCount();i++)
			{
				if(tblGPAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT > 0)
			{
				setMSG("All selections in Gate Pass Authorisation will be lost..",'E');
			}
		
		}
		
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		String L_strINDNO ="";
		String L_strGPPNO ="";
		try
		{
			if(M_objSOURC == btnRPDC3)
			{
				int i=0;
				mm_rpind objINDRP = new mm_rpind(M_strSBSCD.substring(0,2)+hstCODCD.get(tblINAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"00",hstCODCD.get(tblINAUT.getValueAt(i,TBL_STRTP).toString()).toString());
				for(i=0;i<tblINAUT.getRowCount();i++)
				{
					if(tblINAUT.getValueAt(i,TBL_DSPFL).toString().equals("true"))
					{
						L_strINDNO = tblINAUT.getValueAt(i,TBL_DOCNO).toString();
						break;
					}
				}
				objINDRP.getALLREC(L_strINDNO,L_strINDNO,'I',"PI");
				try
				{
					Runtime r = Runtime.getRuntime();
					Process p = null;
					p  = r.exec("c:\\windows\\wordpad.exe "+"c:\\reports\\mm_rpind.doc"); 
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"Error.exescrn.. ");
 				}
				finally
				{
					objINDRP = null;
				}
			}
			if((M_objSOURC == btnRPDC1)||(M_objSOURC == btnRPDC2))
			{
				int i=0;
				if(M_objSOURC == btnRPDC1)
				{
					for(i=0;i<tblGPAPR.getRowCount();i++)
					{
						if(tblGPAPR.getValueAt(i,TBL_DSPFL).toString().equals("true"))
						{
							L_strGPPNO = tblGPAPR.getValueAt(i,TBL_DOCNO).toString();
							break;
						}
					}
					mm_rpgpp objGPPRP = new mm_rpgpp(M_strSBSCD.substring(0,2)+hstCODCD.get(tblGPAPR.getValueAt(i,TBL_STRTP).toString()).toString()+"00");
					objGPPRP.genRPFIL(L_strGPPNO,L_strGPPNO,hstCODCD.get(tblGPAPR.getValueAt(i,TBL_DOCTP).toString()).toString(),tblGPAPR.getValueAt(i,TBL_DOCTP).toString(),tblGPAPR.getValueAt(i,TBL_STRTP).toString());
				}
				else if(M_objSOURC == btnRPDC2)
				{
					i =0;
					for(i=0;i<tblGPAUT.getRowCount();i++)
					{
						if(tblGPAUT.getValueAt(i,TBL_DSPFL).toString().equals("true"))
						{
							L_strGPPNO = tblGPAUT.getValueAt(i,TBL_DOCNO).toString();
							break;
						}
					}
				    mm_rpgpp objGPPRP = new mm_rpgpp(M_strSBSCD.substring(0,2)+hstCODCD.get(tblGPAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"00");
					objGPPRP.genRPFIL(L_strGPPNO,L_strGPPNO,hstCODCD.get(tblGPAUT.getValueAt(i,TBL_DOCTP).toString()).toString(),tblGPAUT.getValueAt(i,TBL_DOCTP).toString(),tblGPAUT.getValueAt(i,TBL_STRTP).toString());
				}
				try
				{
					Runtime r = Runtime.getRuntime();
					Process p = null;
					p  = r.exec("c:\\windows\\wordpad.exe "+"c:\\reports\\mm_rpgpp.doc"); 
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"Error.exescrn.. ");
 				}
				finally
				{
					//objGPPRP = null;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG("Error in generating report ..",'E');
		}
	}
	private void getDOCDT(ResultSet P_rstRSSET,cl_JTable P_tblGPDTL) throws Exception
	{
		int i=0;
		ResultSet L_rstRSSET;
		String L_strSTRTP ="",L_strMGPNO ="",L_strDPTCD ="",L_strMGPTP ="",L_strSTSFL ="";
		P_tblGPDTL.clrTABLE();
		if(P_rstRSSET !=null)
		{
			flgDATA = true;
			while(P_rstRSSET.next())
			{
				L_strSTRTP = nvlSTRVL(P_rstRSSET.getString("GP_STRTP"),"");
				L_strMGPNO = nvlSTRVL(P_rstRSSET.getString("GP_MGPNO"),"");
				L_strMGPTP = nvlSTRVL(P_rstRSSET.getString("GP_MGPTP"),"");
				L_strDPTCD = nvlSTRVL(P_rstRSSET.getString("GP_DPTCD"),"");
				if(hstCODDS.containsKey((String)L_strSTRTP))
				    P_tblGPDTL.setValueAt(hstCODDS.get(L_strSTRTP).toString(),i,TBL_STRTP);
				else
				    P_tblGPDTL.setValueAt(L_strSTRTP,i,TBL_STRTP);
				if(hstCODDS.containsKey((String)L_strMGPTP))
				    P_tblGPDTL.setValueAt(hstCODDS.get(L_strMGPTP).toString(),i,TBL_DOCTP);
				else
				    P_tblGPDTL.setValueAt(L_strMGPTP,i,TBL_DOCTP);
				P_tblGPDTL.setValueAt(L_strMGPNO,i,TBL_DOCNO);
				if(hstCODDS.containsKey((String)L_strDPTCD))
				    P_tblGPDTL.setValueAt(hstCODDS.get(L_strDPTCD).toString(),i,TBL_DPTCD);
				else
				    P_tblGPDTL.setValueAt(L_strDPTCD,i,TBL_DPTCD);
				P_tblGPDTL.setValueAt(nvlSTRVL(P_rstRSSET.getString("GP_APRVL"),"0.00"),i,TBL_APRVL);
				L_strSTSFL = nvlSTRVL(P_rstRSSET.getString("GP_STSFL"),"");
				if(L_strSTSFL.equals("2"))
				{
					L_strSTSFL = "Held For Discussion";
					tblGPAPR.setValueAt(L_strSTSFL,i,TBL_APRFL);
				}
				P_tblGPDTL.setValueAt(nvlSTRVL(P_rstRSSET.getString("GP_VENCD"),""),i,TBL_VENCD);
				M_strSQLQRY ="SELECT RM_DOCTP,RM_REMDS FROM MM_RMMST WHERE RM_STRTP ='"+L_strSTRTP +"'"
							 +" AND RM_TRNTP ='GP' AND RM_DOCTP in('GP','RQ') AND RM_DOCNO ='"+L_strMGPNO +"' AND IFNULL(RM_STSFL,'') <>'X'";										
				L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				String L_strREMDS ="";
				if(L_rstRSSET !=null)
				{
				 	while(L_rstRSSET.next())
					{
						L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
						if(nvlSTRVL(L_rstRSSET.getString("RM_DOCTP"),"").equals("GP"))
							P_tblGPDTL.setValueAt(L_strREMDS,i,TBL_REMDS);
						else if(nvlSTRVL(L_rstRSSET.getString("RM_DOCTP"),"").equals("RQ"))
							P_tblGPDTL.setValueAt(L_strREMDS,i,TBL_COMDS);
					}
				}
				P_tblGPDTL.setValueAt(nvlSTRVL(P_rstRSSET.getString("GP_PREBY"),""),i,TBL_PREBY);
				P_tblGPDTL.setValueAt(nvlSTRVL(P_rstRSSET.getString("GP_VEHNO"),""),i,TBL_VEHNO);
				P_tblGPDTL.setValueAt(nvlSTRVL(P_rstRSSET.getString("GP_VEHDS"),""),i,TBL_VEHDS);
				i++;
			}
		}
	}
	private void getITDTL(cl_JTable P_tblGPDTL,cl_JTable P_tblITMDT) throws Exception 
	{
		int L_intSELROW = P_tblGPDTL.getSelectedRow();
		if(P_tblGPDTL.getValueAt(L_intSELROW,TBL_DSPFL).equals("false"))
		{
			P_tblITMDT.clrTABLE();
			return;
		}
		int i=0;
		for(i=0;i<P_tblGPDTL.getRowCount();i++)
		if(i != L_intSELROW)
			P_tblGPDTL.setValueAt(new Boolean(false),i,TBL_DSPFL);
		M_strSQLQRY = "SELECT GP_MGPNO,GP_MATCD,GP_MGPDT,GP_VENCD,GP_VENNM,GP_DUEDT,GP_STSFL,GP_FRWTO," 
		+"GP_ISSQT,GP_RECQT,GP_DPTCD,GP_VEHNO,GP_VEHDS,GP_GRNNO,GP_GINNO,GP_INSFL,GP_STSFL,GP_PREBY,GP_APRBY,GP_APRVL,GP_PKGCT,CT_MATDS,CT_UOMCD "
		+"FROM MM_GPTRN,CO_CTMST WHERE CT_MATCD = GP_MATCD AND GP_STRTP = '"+hstCODCD.get(P_tblGPDTL.getValueAt(L_intSELROW,TBL_STRTP).toString()).toString()+"' AND "
		+"GP_MGPTP = '"+hstCODCD.get(P_tblGPDTL.getValueAt(L_intSELROW,TBL_DOCTP).toString()).toString()+"' AND GP_MGPNO = '"+P_tblGPDTL.getValueAt(L_intSELROW,TBL_DOCNO).toString()+"' AND GP_STSFL <>'X'";
		P_tblITMDT.clrTABLE();
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		i=0;
		if(M_rstRSSET !=null)
		{
			while(M_rstRSSET.next())
			{
				P_tblITMDT.setValueAt(M_rstRSSET.getString("GP_MATCD"),i,1);
				P_tblITMDT.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,2);
				P_tblITMDT.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,3);
				P_tblITMDT.setValueAt(M_rstRSSET.getString("GP_ISSQT"),i,4);
				datTEMP = M_rstRSSET.getDate("GP_DUEDT");
				if(datTEMP !=null)
				{
					P_tblITMDT.setValueAt(M_fmtLCDAT.format(datTEMP),i++,5);
				}
			}
		}
	}
	private void getINDTL() throws Exception 
	{
		int L_intSELROW = tblINAUT.getSelectedRow();
		if(tblINAUT.getValueAt(L_intSELROW,TBL_DSPFL).equals("false"))
		{
			tblITDT3.clrTABLE();
			return;
		}
		int i=0;
		String L_strTEMP ="";
		for(i=0;i<tblINAUT.getRowCount();i++)
		if(i != L_intSELROW)
			tblINAUT.setValueAt(new Boolean(false),i,TBL_DSPFL);
		M_strSQLQRY = "SELECT IN_MATCD,IN_INDQT,IN_REQDT,IN_EXPDT,IN_TCFFL,IN_INSFL,CT_MATDS,CT_UOMCD "
		+"FROM MM_INMST,CO_CTMST WHERE CT_MATCD = IN_MATCD AND IN_STRTP = '"+hstCODCD.get(tblINAUT.getValueAt(L_intSELROW,TBL_STRTP).toString()).toString()+"'"
		+" AND IN_INDNO = '"+tblINAUT.getValueAt(L_intSELROW,TBL_DOCNO).toString()+"' AND IN_STSFL <>'X'";
		tblITDT3.clrTABLE();
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		i=0;
		if(M_rstRSSET !=null)
		{
			while(M_rstRSSET.next())
			{
				tblITDT3.setValueAt(M_rstRSSET.getString("IN_MATCD"),i,1);
				tblITDT3.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,2);
				tblITDT3.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,3);
				tblITDT3.setValueAt(M_rstRSSET.getString("IN_INDQT"),i,4);
				datTEMP = M_rstRSSET.getDate("IN_REQDT");
				if(datTEMP !=null)
				{
					tblITDT3.setValueAt(M_fmtLCDAT.format(datTEMP),i,5);
				}
				datTEMP = M_rstRSSET.getDate("IN_EXPDT");
				if(datTEMP !=null)
				{
					tblITDT3.setValueAt(M_fmtLCDAT.format(datTEMP),i,6);
				}
				L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IN_TCFFL"),"");
				if(L_strTEMP.equals("Y"))
					tblITDT3.setValueAt(Boolean.TRUE,i,7);
				else
					tblITDT3.setValueAt(Boolean.FALSE,i,7);
				L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IN_INSFL"),"");
				if(L_strTEMP.equals("Y"))
					tblITDT3.setValueAt(Boolean.TRUE,i,8);
				else
					tblITDT3.setValueAt(Boolean.FALSE,i,8);
				i++;
			}
		}
	}
	public void mouseClicked(MouseEvent L_ME)
	{
		try
		{
			super.mouseClicked(L_ME);
			if(M_objSOURC == jtpTRNDT)
			{
				M_strSQLQRY ="";
				if(jtpTRNDT.getSelectedIndex() ==0)
				{
					tblGPAPR.setEnabled(false);
					tblITDT1.setEnabled(false);
					tblGPAPR.cmpEDITR[0].setEnabled(true);
					tblGPAPR.cmpEDITR[1].setEnabled(true);
					tblGPAPR.cmpEDITR[TBL_APRFL].setEnabled(true);
					M_strSQLQRY = "SELECT DISTINCT GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MGPDT,GP_DPTCD,GP_APRVL,GP_VENCD,GP_PREBY,GP_VEHNO,GP_VEHDS,GP_STSFL FROM MM_GPTRN "
						+"WHERE ifnull(GP_STSFL,'') in('1','2') AND GP_FRWTO = '"+cl_dat.M_strUSRCD_pbst+"'";					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					getDOCDT(M_rstRSSET,tblGPAPR);
				}
				else if(jtpTRNDT.getSelectedIndex() ==1)
				{
					tblGPAUT.setEnabled(false);
					tblITDT2.setEnabled(false);
					tblGPAUT.cmpEDITR[0].setEnabled(true);
					tblGPAUT.cmpEDITR[1].setEnabled(true);
					M_strSQLQRY = "SELECT DISTINCT GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MGPDT,GP_DPTCD,GP_APRVL,GP_VENCD,GP_PREBY,GP_VEHNO,GP_VEHDS,GP_STSFL FROM MM_GPTRN "
						+"WHERE ifnull(GP_STSFL,'') = '3'";					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					getDOCDT(M_rstRSSET,tblGPAUT);
				}
				else if(jtpTRNDT.getSelectedIndex() ==2)
				{
					ResultSet L_rstRSSET;
					String L_strSTRTP="",L_strINDNO ="",L_strINDTP="",L_strTEMP ="";
					tblINAUT.setEnabled(false);
					tblITDT3.setEnabled(false);
					tblINAUT.cmpEDITR[0].setEnabled(true);
					tblINAUT.cmpEDITR[1].setEnabled(true);
					tblINAUT.cmpEDITR[TBL_APRFL].setEnabled(true);
					M_strSQLQRY = "SELECT DISTINCT IN_INDTP,IN_STRTP,IN_INDNO,IN_DPTCD,IN_FRWBY,IN_STSFL,sum(IN_INDVL) L_APRVL FROM MM_INMST "
							+"WHERE ifnull(IN_STSFL,'') IN ('2','3') and IN_FRWTO ='"+cl_dat.M_strUSRCD_pbst+"' and ifnull(IN_STSFL,'') <>'X'"
						    +" group by IN_INDTP,IN_STRTP,IN_INDNO,IN_DPTCD,IN_FRWBY,IN_STSFL ORDER BY IN_INDTP,IN_STRTP,IN_INDNO ";
					tblINAUT.clrTABLE();
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					int i=0;
					if(M_rstRSSET !=null)
					{
						while(M_rstRSSET.next())
						{
							L_strSTRTP = M_rstRSSET.getString("IN_STRTP");
							L_strINDNO = M_rstRSSET.getString("IN_INDNO");
							L_strINDTP = M_rstRSSET.getString("IN_INDTP");
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IN_STSFL"),"");
							if(L_strINDTP.equals("01"))
								L_strINDTP = "P.I.";
							else
								L_strINDTP = "R.C.R";
							tblINAUT.setValueAt(hstCODDS.get(L_strSTRTP).toString(),i,TBL_STRTP);
							tblINAUT.setValueAt(L_strINDTP,i,TBL_DOCTP);
							tblINAUT.setValueAt(M_rstRSSET.getString("IN_INDNO"),i,TBL_DOCNO);
							tblINAUT.setValueAt(hstCODDS.get(M_rstRSSET.getString("IN_DPTCD")).toString(),i,TBL_DPTCD);
							tblINAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_APRVL"),"0.00"),i,TBL_APRVL);
							tblINAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_FRWBY"),""),i,TB1_PREBY);
							if(L_strTEMP.equals("3"))
							{
								L_strTEMP = "Held For Discussion";
								tblINAUT.setValueAt(L_strTEMP,i,TBL_APRFL);
							}
							M_strSQLQRY ="SELECT RM_REMTP,RM_REMDS FROM MM_RMMST WHERE RM_STRTP ='"+L_strSTRTP +"'"
							 +" AND RM_TRNTP ='IN' AND RM_REMTP in('IND','OTH') AND RM_DOCNO ='"+L_strINDNO +"' AND IFNULL(RM_STSFL,'') <>'X'";										
							L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
							String L_strREMDS ="";
							if(L_rstRSSET !=null)
							{
								while(L_rstRSSET.next())
								{
									L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
									if(nvlSTRVL(L_rstRSSET.getString("RM_REMTP"),"").equals("IND"))
										tblINAUT.setValueAt(L_strREMDS,i,TBL_REMDS);
									else if(nvlSTRVL(L_rstRSSET.getString("RM_REMTP"),"").equals("OTH"))
										tblINAUT.setValueAt(L_strREMDS,i,TBL_COMDS);
								}
								L_rstRSSET.close();
							}
							i++;
						}
						M_rstRSSET.close();
					}
				}
			}
			else if(M_objSOURC == tblGPAPR)
			{
				
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"Child.ActionPerformed..");	
		}
	}
	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			super.mouseReleased(L_ME);
			if((M_objSOURC == tblGPAPR)&&(tblGPAPR.getSelectedColumn() == TBL_DSPFL))
			{
				setMSG("",'N');
				getITDTL(tblGPAPR,tblITDT1);
			}
			else if((M_objSOURC == tblGPAUT)&&(tblGPAUT.getSelectedColumn() == TBL_DSPFL))
			{
				setMSG("",'N');
				getITDTL(tblGPAUT,tblITDT2);
			}
			else if((M_objSOURC == tblINAUT)&&(tblINAUT.getSelectedColumn() == TBL_DSPFL))
			{
				setMSG("",'N');
				getINDTL();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"mouseReleased");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);		
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
	}
	class inpVRFY extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			if(input == txtTEMP)
			{
				// validation for blank field to be done at save 
				// If error condition return false;
			}
			return true;	
		}
	}
	private class co_teautTBLVR extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(P_intCOLID== 0) // Column ID is 0
				{
					
				}
			}
			catch(Exception L_E)
			{
			}
			return true;
		}
		
	}
	boolean vldDATA()
	{
		try
		{
			setMSG("",'N');
			int L_intSELCT =0;
			if(jtpTRNDT.getSelectedIndex() ==0)
			{
				// GP Approval
				for(int i=0;i<tblGPAPR.getRowCount();i++)
				{
					if(tblGPAPR.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						L_intSELCT++;
						if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals(""))
						{
							setMSG("Please Select the Authorisation Tag at line "+(i+1) + "..",'E');
							return false;
						}
						if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals("Select"))
						{
							setMSG("Please Select the Authorisation Tag at line "+(i+1) + "..",'E');
							return false;
						}
					}
				}
				if(L_intSELCT ==0)
				{
					setMSG("Please select at least one row for Approval..",'E');
					return false;
				}
			}
			else if(jtpTRNDT.getSelectedIndex() ==1)
			{
				// GP Authorisation
				L_intSELCT =0;
				for(int i=0;i<tblGPAUT.getRowCount();i++)
				{
					if(tblGPAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						L_intSELCT++;
						if(tblGPAUT.getValueAt(i,TBL_VEHNO).toString().equals(""))
						{
							setMSG("Vehicle no. for G.P "+tblGPAUT.getValueAt(i,TBL_DOCNO).toString()+" Should be updated by Stores before authorisation..",'E');
							return false;
						}
						if(tblGPAUT.getValueAt(i,TBL_VEHDS).toString().equals(""))
						{
							setMSG("Carrier Name for G.P "+tblGPAUT.getValueAt(i,TBL_DOCNO).toString()+" Should be updated by Stores before authorisation..",'E');
							return false;
						}
					}
				}
				if(L_intSELCT ==0)
				{
					setMSG("Please select at least one row for Authorisation..",'E');
					return false;
				}
			}
			else if(jtpTRNDT.getSelectedIndex() ==2)
			{
				// Indent Authorisation
				L_intSELCT =0;
				for(int i=0;i<tblINAUT.getRowCount();i++)
				{
					if(tblINAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						L_intSELCT++;
						if(tblINAUT.getValueAt(i,TBL_APRFL).toString().equals(""))
						{
							setMSG("Please Select the Authorisation Tag at line "+(i+1) + "..",'E');
							return false;
						}
						if(tblINAUT.getValueAt(i,TBL_APRFL).toString().equals("Select"))
						{
							setMSG("Please Select the Authorisation Tag at line "+(i+1) + "..",'E');
							return false;
						}
					}
				}
				if(L_intSELCT ==0)
				{
					setMSG("Please select at least one row for Authorisation..",'E');
					return false;
				}
			}
			return true;
		}
		catch(Exception L_E)
		{
			return false;
		}
	}
	void exeSAVE()
	{
		try
		{
			if(!vldDATA())
				return;
			//cl_dat.M_strLCUPD_pbst = true;
			String L_strEML ="",L_strMGPNO ="",L_strINDNO="",L_strSTSFL ="",L_strAPRDS="";
			String L_strGPSTS="",L_strGPAPR ="";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				cl_eml ocl_eml = new cl_eml();
				if(jtpTRNDT.getSelectedIndex() ==0)
				{
					for(int i=0;i<tblGPAPR.getRowCount();i++)
					{
						if(tblGPAPR.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{	
							L_strMGPNO =tblGPAPR.getValueAt(i,TBL_DOCNO).toString();
							L_strGPSTS ="";
							if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals("Held For Discussion"))
							{
								L_strGPAPR = "Held For Discussion";
								L_strGPSTS ="2";
							}
							else if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals("Approved"))
							{
								L_strGPAPR = "Approved";
								L_strGPSTS ="3";
							}
							if(L_strGPSTS.equals(""))
							   continue; // Next Iteration

							M_strSQLQRY ="UPDATE MM_GPTRN SET GP_STSFL = '"+L_strGPSTS+"',GP_LUSBY = '"
							+cl_dat.M_strUSRCD_pbst+"',GP_LUPDT = '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"
							+"GP_APRBY = '"+cl_dat.M_strUSRCD_pbst+"',GP_APRDT = current_timestamp "
							+"WHERE GP_STRTP = '"+hstCODCD.get(tblGPAPR.getValueAt(i,TBL_STRTP).toString()).toString()+"' AND GP_MGPTP = '"
							+hstCODCD.get(tblGPAPR.getValueAt(i,TBL_DOCTP).toString()).toString()+"' AND GP_MGPNO = '"
							+L_strMGPNO+"' and ifnull(GP_STSFL,'') <>'X'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							// Generate email to user saying GP no. is approved.
							// keep two variables preby and aprby, frwto 
							M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+tblGPAPR.getValueAt(i,TBL_PREBY).toString()+"'";
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET != null)
							if(M_rstRSSET.next())
							{
								L_strEML = M_rstRSSET.getString("US_EMLRF");
								if(L_strEML.length() >0)
									ocl_eml.sendfile(L_strEML,null,"Intimation of Gate Pass Approval","Gate Pass No."+L_strMGPNO + " is "+L_strGPAPR +" by "+cl_dat.M_strUSRCD_pbst);
							}
							if(M_rstRSSET != null)
								M_rstRSSET.close();
						}
					}
				}
				else if(jtpTRNDT.getSelectedIndex() ==1)
				{
					for(int i=0;i<tblGPAUT.getRowCount();i++)
					{
						if(tblGPAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							L_strMGPNO = tblGPAUT.getValueAt(i,TBL_DOCNO).toString();
							M_strSQLQRY ="UPDATE MM_GPTRN SET GP_STSFL = '4',GP_LUSBY = '"
							+cl_dat.M_strUSRCD_pbst+"',GP_LUPDT = '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"
							+"GP_AUTBY = '"+cl_dat.M_strUSRCD_pbst+"',GP_AUTDT = current_timestamp "
							+"WHERE GP_STRTP = '"+hstCODCD.get(tblGPAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"' AND GP_MGPTP = '"
							+hstCODCD.get(tblGPAUT.getValueAt(i,TBL_DOCTP).toString()).toString()+"' AND GP_MGPNO = '"
							+L_strMGPNO+"' and ifnull(GP_STSFL,'') <>'X'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(hstCODCD.get(tblGPAUT.getValueAt(i,TBL_DOCTP).toString()).toString().equals("51")) //Returnable
							{
								M_strSQLQRY = "update mm_gptrn set gp_duedt = date(days(gp_duedt)+days(CURRENT_DATE)-days(GP_MGPDT)) "
										+"WHERE GP_STRTP = '"+hstCODCD.get(tblGPAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"' AND GP_MGPTP = '51'"
										+" AND GP_MGPNO = '"+L_strMGPNO+"' and ifnull(GP_STSFL,'') <>'X'";
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							}
							// Generate email to user saying GP no. is authorised.
							M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+tblGPAUT.getValueAt(i,TBL_PREBY).toString()+"'";;
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET != null)
							while(M_rstRSSET.next())
							{
								L_strEML = M_rstRSSET.getString("US_EMLRF");
								if(L_strEML.length() >0)
									ocl_eml.sendfile(L_strEML,null,"Intimation of Gate Pass Authorisation","Gate Pass No."+ L_strMGPNO + " is Authorised for Gate out. ");
							}
							if(M_rstRSSET != null)
							M_rstRSSET.close();
						}
					}
				}
				else if(jtpTRNDT.getSelectedIndex() ==2)
				{
					for(int i=0;i<tblINAUT.getRowCount();i++)
					{
						if(tblINAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							L_strINDNO = tblINAUT.getValueAt(i,TBL_DOCNO).toString();
							if(tblINAUT.getValueAt(i,TBL_APRFL).toString().equals("Held For Discussion"))
							{
								L_strAPRDS = "Held For Discussion";
								L_strSTSFL ="3";
							}
							else if(tblINAUT.getValueAt(i,TBL_APRFL).toString().equals("Authorised"))
							{
								L_strAPRDS = "Authorised";
								L_strSTSFL ="4";
							}
							if(L_strSTSFL.equals(""))
							   continue; // Next Iteration
							M_strSQLQRY ="UPDATE MM_INMST SET IN_STSFL = '"+L_strSTSFL +"',IN_LUSBY = '"
							+cl_dat.M_strUSRCD_pbst+"',IN_LUPDT = '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"
							+"IN_AUTBY = '"+cl_dat.M_strUSRCD_pbst+"',IN_AUTDT = current_timestamp ";
							if(L_strSTSFL.equals("4"))
							{
								M_strSQLQRY +=",IN_AUTQT = IN_INDQT ";	
							}
							M_strSQLQRY +=" WHERE IN_STRTP = '"+hstCODCD.get(tblINAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"'"
							+" AND IN_INDNO = '"+L_strINDNO+"' and ifnull(IN_STSFL,'') <>'X'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(L_strSTSFL.equals("4"))
							{
								M_strSQLQRY = "update mm_inmst set in_porby = date(days(in_porby)+days(CURRENT_DATE)-days(IN_INDDT)),in_reqdt = date(days(in_reqdt)+days(CURRENT_DATE)-days(IN_INDDT)) "
											 +",in_expdt = date(days(in_expdt)+days(CURRENT_DATE)-days(IN_INDDT))"
										+"WHERE IN_STRTP = '"+hstCODCD.get(tblINAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"'"
										+" AND IN_INDNO = '"+L_strINDNO+"' AND ifnull(IN_INDTP,'') ='01'";
								cl_dat.exeSQLUPD(M_strSQLQRY,"");
							}
							// Generate email to user saying Indent is authorised.
							M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+tblINAUT.getValueAt(i,TB1_PREBY).toString()+"'";;
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET != null)
							while(M_rstRSSET.next())
							{
								L_strEML = M_rstRSSET.getString("US_EMLRF");
								if(L_strEML.length() >0)
									ocl_eml.sendfile(L_strEML,null,"Intimation of Indent Authorisation","Indent No."+ L_strINDNO + " is "+L_strAPRDS);
							}
							if(M_rstRSSET != null)
							M_rstRSSET.close();
						}
					}
				}

				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setMSG("Data saved successfully..",'N');
					if(jtpTRNDT.getSelectedIndex() ==0)
					{
						tblGPAPR.clrTABLE();
						tblITDT1.clrTABLE();
					}
					else if(jtpTRNDT.getSelectedIndex() ==1)
					{
						tblGPAUT.clrTABLE();
						tblITDT2.clrTABLE();
					}
					else if(jtpTRNDT.getSelectedIndex() ==2)
					{
						tblINAUT.clrTABLE();
						tblITDT3.clrTABLE();
					}
					flgDATA =false;	
				}
				else
				{
					setMSG("Error in Saving..",'E');
				}
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");			
		}
	}
}