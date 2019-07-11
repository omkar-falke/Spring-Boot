import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JCheckBox;
import java.awt.event.FocusAdapter.*;import javax.swing.JTable.*;import javax.swing.JTable;import javax.swing.InputVerifier;
import javax.swing.JComponent;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Hashtable;import java.awt.Color;
import java.sql.ResultSet;import javax.swing.JPanel;import javax.swing.JTabbedPane;
import java.sql.CallableStatement;import javax.swing.JComboBox;
import javax.swing.JOptionPane;import java.awt.event.MouseEvent;



/*
System Name					: Marketing System
Program Name				: Customer Permit Form
Program Desc.				: 
Author						: Mr. Aawaj M. Jain
Date						: 
Version						: 
Special functionality used	: 

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

Purpose : This system captures movement detail of permit form, which is received from customer. 
		  The detail recorded are used for generating status report of permit form at any given point of time.
		  Transactios of receipt and issue of permit form at different location are stored in pemit form transaction table.

List of tables used :
Table Name     Primary key										Operation done
															Insert   Update   Query   Delete	
---------------------------------------------------------------------------------------------
MR_PFTRN       PFT_PRTTP,PFT_PRTCD,PFT_FRMTP,PFT_FRMNO			#	    #       #       #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD								    #	
CO_PTMST       PT_PRTTP,PT_PRTCD												#
---------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
---------------------------------------------------------------------------------------------
txtPRTTP    PFT_PRTTP       MR_PFTRN      VARCHAR(1)     Party Type
txtPRTCD    PFT_PRTCD		MR_PFTRN	  VARCHAR(5)	 Party Code
txtFRMTP	PFT_FRMTP		MR_PFTRN	  VARCHAR(1)	 Form Type

TB1_FRMNO	PFT_FRMNO		MR_PFTRN	  VARCHAR(8)	 Form no
TB1_ISSDT	PFT_ISSDT		MR_PFTRN	  DATE			 issue date	
TB1_EXPDT	PFT_EXPDT		MR_PFTRN	  DATE			 Expiery date	
TB1_DLHDT	PFT_DLHDT		MR_PFTRN	  DATE			 Delhi Rct. date
TB1_CMSDT	PFT_CMSDT		MR_PFTRN	  DATE			 CMS Rct date
TB1_NGTDT	PFT_NGTDT		MR_PFTRN	  DATE			 Nagothane Rct. Date
---------------------------------------------------------------------------------------------

List of Fields with help facility
Field Name  Display Description         Display Columns           Table Name
---------------------------------------------------------------------------------------------
txtPRTTP    Party Type,Description      cmt_codcd,cmt_codds		  CO_CDTRN-MST/COXXPRT
txtPRTCD	Party Code,Party Name		pt_prtcd,pt_prtnm		  CO_PTMST
txtFRMTP	Form Type,Description		cmt_codcd,cmt_codds		  CO_CDTRN-SYS/MRXXFTP		
---------------------------------------------------------------------------------------------

Validations :
    - LUSBY & LUPDT columns to be updated in addition, modification & eletion mode.
    - After specifying Party Code & Form Type all Existing permit form records (against which despatch has not taken place)
	  will be displayed in entry table. i.e. records with pft_invdt is null.
	- Entried for TB!_DLHDT,TB1_CMSDT,TB1_NGTDT should not exceed current date.

Other Requirements:
	- User can insert new record by adding new entry in entry table.
    - Available stock below the entry table indicatres no. of forms avail at perticular stage.
	- Addition/modification option merge under addn option.
	- Under deletion option user will have facility to mark the record for deletion	(PFT_STSFL = 'X')


*/




public class mr_tepfm extends cl_pbase
{
	JTextField txtPRTTP,txtPRTCD,txtFRMTP;
	JLabel lblPRTNM;
	JLabel lblDLHCT,lblCMSCT,lblNGTCT,lblFRMDS;
	private cl_JTable tblTEPFM; 
	private final int TB_CHKFL=0;
	private final int TB1_FRMNO=1;
	private final int TB1_ISSDT=2;
	private final int TB1_EXPDT=3;
	private final int TB1_DLHDT=4;
	private final int TB1_CMSDT=5;
	private final int TB1_NGTDT=6;
	private TBLINPVF objTBLVRF; 
	private String strTEMP;
	private int intRWCNT=0,intCNT1=0;
	
	private JTextField txtFRMNO;
	private JTextField txtISSDT;
	private JTextField txtEXPDT;
	private JTextField txtDLHDT;
	private JTextField txtCMSDT;
	private JTextField txtNGTDT;
	
	mr_tepfm()
	{
	    super(2);
	    try
	    {
	 		lblPRTNM=new JLabel("");
			
			lblDLHCT=new JLabel("");
			lblCMSCT=new JLabel("");
			lblNGTCT=new JLabel("");
			lblFRMDS=new JLabel("");
				
			setMatrix(20,20);
			
			add(new JLabel("Party Type "),2,5,1,3,this,'L');     
			add(txtPRTTP=new TxtLimit(1),2,7,1,3,this,'L');
			
			add(new JLabel("Party Code "),3,5,1,3,this,'L');     
			add(txtPRTCD=new TxtLimit(5),3,7,1,3,this,'L');
			
			add(lblPRTNM,3,10,1,8,this,'L');     
			
			add(new JLabel("Form Type"),4,5,1,3,this,'L');
			add(txtFRMTP=new TxtLimit(2),4,7,1,3,this,'L');
			
			add(lblFRMDS,4,10,1,3,this,'L');
			
			String[] L_strTBLHD1 = {" ","Form No.","Issued Date","Valid Upto","Recd at Delhi ON ","Recd at CMS ON","Received at Site ON"};
			int[] L_intCOLSZ1 = {15,150,150,150,150,150,135};
			tblTEPFM = crtTBLPNL1(this,L_strTBLHD1,500,8,3,7,15,L_intCOLSZ1,new int[]{0});
			
			add(new JLabel("Available Stock"),15,3,1,3,this,'L');
			add(new JLabel("DELHI :"),16,3,1,3,this,'L');		
			add(lblDLHCT  ,16,5,1,2, this,'L');
			add(new JLabel("CMS   :"),17,3,1,3,this,'L');		
			add(lblCMSCT  ,17,5,1,2,this,'L');
			add(new JLabel("NGT   :"),18,3,1,3,this,'L');		
			add(lblNGTCT  ,18,5,1,2,this,'L');
			
			tblTEPFM.setCellEditor(TB1_FRMNO,txtFRMNO=new TxtLimit(8));
			tblTEPFM.setCellEditor(TB1_ISSDT,txtISSDT=new TxtDate());
			tblTEPFM.setCellEditor(TB1_EXPDT,txtEXPDT=new TxtDate());
			tblTEPFM.setCellEditor(TB1_DLHDT,txtDLHDT=new TxtDate());
			tblTEPFM.setCellEditor(TB1_CMSDT,txtCMSDT=new TxtDate());
			tblTEPFM.setCellEditor(TB1_NGTDT,txtNGTDT=new TxtDate());
			INPVF oINPVF=new INPVF();
			objTBLVRF = new TBLINPVF();
			txtFRMNO.addKeyListener(this);
			txtPRTTP.setInputVerifier(oINPVF);
			txtPRTCD.setInputVerifier(oINPVF);
			txtFRMTP.setInputVerifier(oINPVF);
			
			txtFRMNO.addKeyListener(this);
			txtISSDT.addKeyListener(this);
			txtEXPDT.addKeyListener(this);
			txtDLHDT.addKeyListener(this);
			txtCMSDT.addKeyListener(this);
			txtNGTDT.addKeyListener(this);
			
			tblTEPFM.setInputVerifier(objTBLVRF);
			
			txtFRMNO.addFocusListener(this);
			txtISSDT.addFocusListener(this);
			txtEXPDT.addFocusListener(this);
			txtDLHDT.addFocusListener(this);
			txtCMSDT.addFocusListener(this);
			txtNGTDT.addFocusListener(this);
			tblTEPFM.addMouseListener(this);
			/*for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objINVER);
			}*/
			
			setENBL(true);
			txtPRTTP.requestFocus();
		}catch(Exception L_EX)
	    {
	        setMSG(L_EX,"Constructor");
	    }
	}
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			//System.out.println("focus gained");
			//System.out.println(">>>>"+intCNT1);
			if(M_objSOURC == tblTEPFM.cmpEDITR[TB1_FRMNO])
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					if(tblTEPFM.getSelectedRow()<intCNT1)
					{	
						((JTextField)tblTEPFM.cmpEDITR[TB1_FRMNO]).setEditable(false);
						/*if(tblTEPFM.getValueAt(tblTEPFM.getSelectedRow(),TB1_FRMNO).toString().length() > 0)
							((JTextField)tblTEPFM.cmpEDITR[TB1_FRMNO]).setEditable(false);
						else
							((JTextField)tblTEPFM.cmpEDITR[TB1_FRMNO]).setEditable(true);*/
					}
					else
							((JTextField)tblTEPFM.cmpEDITR[TB1_FRMNO]).setEditable(true);
				}
			}
		}	
		catch(Exception E)
		{
			setMSG(E,"FocusGained");			
		}
	}
	
	
	void setENBL(boolean L_flgSTAT)
	{
	    super.setEnabled( L_flgSTAT);
		tblTEPFM.setEnabled(L_flgSTAT);
		txtPRTTP.setEnabled(true);
		txtPRTCD.setEnabled(true);
		txtFRMTP.setEnabled(true);
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		//System.out.println("actionperformed");
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))		
				{
					setENBL(false);	
					((JCheckBox)tblTEPFM.cmpEDITR[TB_CHKFL]).setEnabled(true);
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
				{
					//System.out.println("its in action performed2");
					((JTextField)tblTEPFM.cmpEDITR[TB1_FRMNO]).setEditable(false);
				}	
				txtPRTTP.requestFocus();
				lblPRTNM.setText("");
				lblFRMDS.setText("");
				lblDLHCT.setText("");
				lblCMSCT.setText("");
				lblNGTCT.setText("");	
				//tblTEPFM.setRowSelectionInterval(0,0);
				//tblTEPFM.setColumnSelectionInterval(1,1);
				clrCOMP();
			}	
			if(M_objSOURC == txtFRMTP)
			{
				getDATA();
			}
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is action Performed");
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
	    super.keyPressed( L_KE);
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				int L_inaCOLSZ[]={60,70,60,60,60,60,60,60,60};
				
				if(M_objSOURC==txtPRTTP)
				{
					M_strHLPFLD="txtPRTTP";
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
					{
						M_strSQLQRY=" Select distinct pft_prttp,cmt_codds from MR_PFTRN,CO_CDTRN where";
						M_strSQLQRY+=" CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPRT' and PFT_PRTTP=cmt_codcd and PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  order by pft_prttp";
					}
					else
						M_strSQLQRY="Select distinct cmt_codcd,cmt_codds from CO_CDTRN where  CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPRT' order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Type", " Description"},2,"CT",new int[]{107,400});
				}		
				
				if(M_objSOURC==txtPRTCD)
				{
					M_strHLPFLD="txtPRTCD";
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
					{
						M_strSQLQRY="Select distinct pft_prtcd,pt_prtnm from MR_PFTRN,CO_PTMST  where ";
						M_strSQLQRY+="PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PFT_PRTTP = '"+txtPRTTP.getText()+"' and ";
						M_strSQLQRY+="PFT_PRTCD like '"+txtPRTCD.getText().trim().toUpperCase()+"%' and PFT_PRTTP=pt_prttp and PFT_PRTCD=PT_PRTCD";
					}	
					else
						M_strSQLQRY="Select distinct pt_prtcd,pt_prtnm from CO_PTMST  where PT_PRTTP = '"+txtPRTTP.getText()+"' and PT_PRTCD like '"+txtPRTCD.getText().trim().toUpperCase()+"%'";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						M_strSQLQRY += "  and PT_PRTTP + PT_PRTCD in (select PFT_PRTTP + PFT_PRTCD from MR_PFTRN where PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PFT_STSFL,'') <> 'X')";
					
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
						M_strSQLQRY += " order by pt_prtnm";
					else 
						M_strSQLQRY += " order by pt_prtnm";
					//System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"CODE", " Name"},2,"CT",new int[]{107,400});
				}		
				
				if(M_objSOURC==txtFRMTP)
				{
					M_strHLPFLD="txtFRMTP";
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
					{
						M_strSQLQRY="Select distinct cmt_codcd,cmt_codds from CO_CDTRN where  CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MRXXFTP'";
						M_strSQLQRY += "  and CMT_CODCD in (select PFT_FRMTP from MR_PFTRN where ";
						M_strSQLQRY += " PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PFT_PRTCD= '"+txtPRTCD.getText()+"' and ";
						M_strSQLQRY += " PFT_PRTTP= '"+txtPRTTP.getText()+"' )";
					}	
					else
						M_strSQLQRY="Select distinct cmt_codcd,cmt_codds from CO_CDTRN where  CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MRXXFTP'";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						M_strSQLQRY += "  and CMT_CODCD in (select PFT_FRMTP from MR_PFTRN where PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PFT_PRTTP= '"+txtPRTTP.getText()+"' and PFT_PRTCD = '"+txtPRTCD.getText()+"'and isnull(PFT_STSFL,'') <> 'X')";
					M_strSQLQRY += "  order by CMT_CODCD";
					//System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Form Type", " Description"},2,"CT",new int[]{107,400});
				}		
			}
		
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC==txtPRTTP)
				{
						txtPRTCD.requestFocus();					
				}	
				if(M_objSOURC==txtPRTCD)
				{
					txtFRMTP.requestFocus();
				}	
				if(M_objSOURC==txtFRMTP)
				{
					txtFRMTP.setText(txtFRMTP.getText().trim().toUpperCase());
					//lblFRMDS.requestFocus();
					tblTEPFM.clrTABLE();
					if(tblTEPFM.isEditing())
						tblTEPFM.getCellEditor().stopCellEditing();
					tblTEPFM.setRowSelectionInterval(0,0);
					tblTEPFM.setColumnSelectionInterval(0,0);
				}	
			}	
	     }catch(Exception L_EX)
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
			if(M_strHLPFLD.equals("txtPRTTP"))
			{
				txtPRTTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtPRTCD"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtPRTCD.setText(L_STRTKN1.nextToken());
				lblPRTNM.setText(L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD.equals("txtFRMTP"))
			{
				StringTokenizer L_STRTKN1=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtFRMTP.setText(L_STRTKN1.nextToken());
				lblFRMDS.setText(L_STRTKN1.nextToken());
			}
		
		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}	
	}
	void getDATA()
	{
		int L_cntDLHCT=0,L_cntCMSCT=0,L_cntNGTCT=0,L_intCNT=0;
		intCNT1=0;
		//System.out.println("inside getDATA()");
			
		M_strSQLQRY="SELECT PFT_FRMNO,PFT_ISSDT,PFT_EXPDT,PFT_DLHDT,PFT_CMSDT,PFT_NGTDT FROM MR_PFTRN WHERE ";
		M_strSQLQRY+="PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PFT_PRTTP='"+txtPRTTP.getText().trim()+"' ";
		M_strSQLQRY+="AND PFT_PRTCD='"+txtPRTCD.getText().trim()+"' ";
		M_strSQLQRY+="AND PFT_FRMTP='"+txtFRMTP.getText().trim()+"' ";
		M_strSQLQRY+="AND isnull(PFT_STSFL,'')<>'X' ";
		
		//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{
			M_strSQLQRY+=" and isnull(PFT_INVNO,'') = '' ";
		}	
		
		M_strSQLQRY+="ORDER BY PFT_FRMNO";
		M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
		
		//System.out.println("------------"+M_strSQLQRY);
		//System.out.println("------------"+M_rstRSSET);
	
		try
		{
		if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					tblTEPFM.setValueAt(M_rstRSSET.getString("PFT_FRMNO"),L_intCNT,TB1_FRMNO);
					if(M_rstRSSET.getDate("PFT_ISSDT")==null)
						tblTEPFM.setValueAt("",L_intCNT,TB1_ISSDT);
					else	
						tblTEPFM.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_ISSDT")),L_intCNT,TB1_ISSDT);
					
					if(M_rstRSSET.getDate("PFT_EXPDT")==null)
						tblTEPFM.setValueAt("",L_intCNT,TB1_EXPDT);
					else	
					tblTEPFM.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_EXPDT")),L_intCNT,TB1_EXPDT);
					
					if(M_rstRSSET.getDate("PFT_DLHDT")==null)
						tblTEPFM.setValueAt("",L_intCNT,TB1_DLHDT);
					else	
					tblTEPFM.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_DLHDT")),L_intCNT,TB1_DLHDT);
					
					if(M_rstRSSET.getDate("PFT_CMSDT")==null)
						tblTEPFM.setValueAt("",L_intCNT,TB1_CMSDT);
					else	
					tblTEPFM.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_CMSDT")),L_intCNT,TB1_CMSDT);
					
					if(M_rstRSSET.getDate("PFT_NGTDT")==null)
						tblTEPFM.setValueAt("",L_intCNT,TB1_NGTDT);
					else	
					tblTEPFM.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("PFT_NGTDT")),L_intCNT,TB1_NGTDT);
					
					L_intCNT++;
					//System.out.println("...."+L_intCNT);
					intCNT1=L_intCNT;
					if(M_rstRSSET.getDate("PFT_NGTDT")==null)
					{
						if(M_rstRSSET.getDate("PFT_CMSDT")!=null)
						{
							L_cntCMSCT++;	
						}	
						else if(M_rstRSSET.getDate("PFT_DLHDT")!=null)
							L_cntDLHCT++;
					}	
					else L_cntNGTCT++;
					
					lblDLHCT.setText(""+L_cntDLHCT);
					lblCMSCT.setText(""+L_cntCMSCT);
					lblNGTCT.setText(""+L_cntNGTCT);
				}	
				
			}	
		}catch(Exception L_EX){
			setMSG(L_EX,"getDATA()");
		}
	}	

	boolean vldDATA()
	{
		try
		{
			if(txtPRTTP.getText().trim().length() ==0)
    			{
					txtPRTTP.requestFocus();
    				setMSG("Enter Party Type",'E');
    				return false;
    			}
			if(txtPRTCD.getText().trim().length() ==0)
    			{
					//txtPRTCD.setEnabled(true);
					txtPRTCD.requestFocus();
    				setMSG("Enter the Party Code",'E');
    				return false;
    			}
			if(txtFRMTP.getText().trim().length() ==0)
    			{
					//txtFRMTP.setEnabled(true);	
					txtFRMTP.requestFocus();
    				setMSG("Enter the Form Type",'E');
    				return false;
    			}
			for(int i=0;i< tblTEPFM.getRowCount();i++)
			{
				if(tblTEPFM.getValueAt(i,TB_CHKFL).toString().equals("true"))
				{
					if(tblTEPFM.getValueAt(i,TB1_FRMNO).toString().length() == 0)
					{
						setMSG("Enter the Form Number",'E');
						return false;
					}
				}
					
			}
		}
		catch(Exception L_EX)
		 {
			setMSG(L_EX,"This is vldDATA");
		 }	
		return true;
	}	

	void insRECRD(int P_intROWNO)
	{
			String strSTSFL="0";
			cl_dat.M_flgLCUPD_pbst = true;
						
			try
			{	
				/*M_strSQLQRY="select PFT_PRTTP from MR_PFTRN where";
				M_strSQLQRY+=" PFT_PRTTP='"+txtPRTTP.getText()+"' and";
				M_strSQLQRY+=" PFT_PRTCD='"+txtPRTCD.getText()+"' and";
				M_strSQLQRY+=" PFT_FRMTP='"+txtFRMTP.getText()+"' and";
				M_strSQLQRY+=" PFT_FRMNO='"+tblTEPFM.getValueAt(P_intROWNO,TB1_FRMNO).toString()+"' ";
				
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				*/
				
				if(P_intROWNO<intCNT1)
				{
					updQUERY(P_intROWNO);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
				else
				{	
					M_strSQLQRY="insert into MR_PFTRN(PFT_CMPCD,PFT_PRTTP,PFT_PRTCD,PFT_FRMTP,PFT_FRMNO,PFT_ISSDT,PFT_EXPDT,PFT_DLHDT,PFT_CMSDT,PFT_NGTDT,PFT_TRNFL,PFT_STSFL,PFT_LUSBY,PFT_LUPDT) values ( ";
					M_strSQLQRY+="'"+cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY+="'"+txtPRTTP.getText().trim()+"',";
					M_strSQLQRY+="'"+txtPRTCD.getText().trim()+"',";
					M_strSQLQRY+="'"+txtFRMTP.getText().trim()+"',";
					M_strSQLQRY+="'"+tblTEPFM.getValueAt(P_intROWNO,TB1_FRMNO).toString() +"',";
					
					if(tblTEPFM.getValueAt(P_intROWNO,TB1_ISSDT).toString()=="")
						M_strSQLQRY+="null,";
					else
						M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEPFM.getValueAt(P_intROWNO,TB1_ISSDT).toString()))+"',";
					
					if(tblTEPFM.getValueAt(P_intROWNO,TB1_EXPDT).toString()=="")
						M_strSQLQRY+="null,";
					else
						M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEPFM.getValueAt(P_intROWNO,TB1_EXPDT).toString()))+"',";
					
					if(tblTEPFM.getValueAt(P_intROWNO,TB1_DLHDT).toString()=="")
						M_strSQLQRY+="null,";
					else
						M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEPFM.getValueAt(P_intROWNO,TB1_DLHDT).toString()))+"',";
					
					if(tblTEPFM.getValueAt(P_intROWNO,TB1_CMSDT).toString()=="")
						M_strSQLQRY+="null,";
					else
						M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEPFM.getValueAt(P_intROWNO,TB1_CMSDT).toString()))+"',";
					
					if(tblTEPFM.getValueAt(P_intROWNO,TB1_NGTDT).toString()=="")
						M_strSQLQRY+="null,";
					else
						M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEPFM.getValueAt(P_intROWNO,TB1_NGTDT).toString()))+"',";
					
					M_strSQLQRY += getUSGDTL("PFT_",'I',strSTSFL)+")";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					//System.out.println(">>"+intCNT1+">>"+M_strSQLQRY);
				}			
			}catch(Exception L_EX)
			 {
				setMSG(L_EX,"This is insRECRD()");
			 }
	}
	

	void updQUERY(int P_intROWNO)
	{
		try{
				M_strSQLQRY  ="update MR_PFTRN SET ";
				if(tblTEPFM.getValueAt(P_intROWNO,TB1_ISSDT).toString()=="")
					M_strSQLQRY+="PFT_ISSDT=null,";
				else
					M_strSQLQRY+="PFT_ISSDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEPFM.getValueAt(P_intROWNO,TB1_ISSDT).toString()))+"',";
							
				if(tblTEPFM.getValueAt(P_intROWNO,TB1_EXPDT).toString()=="")
					M_strSQLQRY+="PFT_EXPDT=null,";
				else
					M_strSQLQRY+="PFT_EXPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEPFM.getValueAt(P_intROWNO,TB1_EXPDT).toString()))+"',";
							
				if(tblTEPFM.getValueAt(P_intROWNO,TB1_DLHDT).toString()=="")
					M_strSQLQRY+="PFT_DLHDT=null,";
				else
					M_strSQLQRY+="PFT_DLHDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEPFM.getValueAt(P_intROWNO,TB1_DLHDT).toString()))+"',";
							
				if(tblTEPFM.getValueAt(P_intROWNO,TB1_CMSDT).toString()=="")
					M_strSQLQRY+="PFT_CMSDT=null,";
				else	
					M_strSQLQRY+="PFT_CMSDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEPFM.getValueAt(P_intROWNO,TB1_CMSDT).toString()))+"',";
							
				if(tblTEPFM.getValueAt(P_intROWNO,TB1_NGTDT).toString()=="")
					M_strSQLQRY+="PFT_NGTDT=null";
				else	
					M_strSQLQRY+="PFT_NGTDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEPFM.getValueAt(P_intROWNO,TB1_NGTDT).toString()))+"'";
							
				//M_strSQLQRY +="PFT_STSFL= 'X'";
				M_strSQLQRY += " WHERE PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PFT_PRTTP = '"+txtPRTTP.getText().trim()+ "'";
				M_strSQLQRY += " AND   PFT_PRTCD = '"+txtPRTCD.getText().trim()+"'";
				M_strSQLQRY += " AND   PFT_FRMTP = '"+txtFRMTP.getText().trim() + "'";
				M_strSQLQRY += " AND   PFT_FRMNO = '"+tblTEPFM.getValueAt(P_intROWNO,TB1_FRMNO)+"'";
			}
			catch(Exception L_EX)
			 {
				setMSG(L_EX,"This is updQUERY");
			 }	
	}	
	
	void exeSAVE()
	{
		int P_intROWNO;
	    try
	    {
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				for(P_intROWNO=0;P_intROWNO<tblTEPFM.getRowCount();P_intROWNO++)                                                                                                           
				{
					if(tblTEPFM.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
					{
						try
						{	
							M_strSQLQRY  ="update MR_PFTRN SET ";
							M_strSQLQRY +="PFT_STSFL= 'X'";
							M_strSQLQRY += " WHERE PFT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PFT_PRTTP = '"+txtPRTTP.getText().trim()+ "'";
							M_strSQLQRY += " AND   PFT_PRTCD = '"+txtPRTCD.getText().trim()+"'";
							M_strSQLQRY += " AND   PFT_FRMTP = '"+txtFRMTP.getText().trim() + "'";
							M_strSQLQRY += " AND   PFT_FRMNO = '"+tblTEPFM.getValueAt(P_intROWNO,TB1_FRMNO)+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
						catch(Exception L_EX)
						{
							setMSG(L_EX,"This is save");
						}
					}
				}
				if(!cl_dat.M_flgLCUPD_pbst)
				  	return;
			}
			
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				for(P_intROWNO=0;P_intROWNO<tblTEPFM.getRowCount();P_intROWNO++)
				{
					if(tblTEPFM.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
					{
						try
						{	
							//System.out.println("inside modRECRD");
							updQUERY(P_intROWNO);
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
								
						}
						catch(Exception L_EX)
						{
							setMSG(L_EX,"This is save");
						}
					}
				}
				if(!cl_dat.M_flgLCUPD_pbst)
				  	return;
			}			
			
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
	    		for(P_intROWNO=0;P_intROWNO<tblTEPFM.getRowCount();P_intROWNO++)
				{
					if(tblTEPFM.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
					{
						insRECRD(P_intROWNO);
					}
				}
			}	
			
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("record updated successfully",'N');
				tblTEPFM.clrTABLE();			
				clrCOMP();

			}
			else
			{
			   	JOptionPane.showMessageDialog(this,"Error in modifying data ","Error",JOptionPane.INFORMATION_MESSAGE);
			    setMSG("Error in updating data..",'E');
			  	//cl_dat.M_btnSAVE_pbst.setEnabled(false);
			}
					
			lblPRTNM.setText("");
			lblFRMDS.setText("");
			lblDLHCT.setText("");
			lblCMSCT.setText("");
			lblNGTCT.setText("");
		}catch(Exception L_EX)
			{
			    setMSG(L_EX,"This is exeSave");
			}
	}
	
	
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			try
			{
				String L_strDOCNO="",newVAL="";
				java.sql.Date L_datTMPDT;
				if(getSource()==tblTEPFM)
				{
					//System.out.println(">>>>>inside TBLINPVF");
					//if(((JTextField)tblTEPFM.cmpEDITR[P_intCOLID]).getText().trim().length()==0)
					//	return true;
					/*if(tblTEPFM.getValueAt(tblTEPFM.getSelectedRow(),TB1_ISSDT).toString().length()==0)
						return true;
					if(tblTEPFM.getValueAt(tblTEPFM.getSelectedRow(),TB1_EXPDT).toString().length()==0)
						return true;
					if(tblTEPFM.getValueAt(tblTEPFM.getSelectedRow(),TB1_DLHDT).toString().length()==0)
						return true;
					if(tblTEPFM.getValueAt(tblTEPFM.getSelectedRow(),TB1_CMSDT).toString().length()==0)
						return true;
					if(tblTEPFM.getValueAt(tblTEPFM.getSelectedRow(),TB1_NGTDT).toString().length()==0)
						return true;*/

					if(P_intCOLID==TB1_FRMNO)
					{
						newVAL=tblTEPFM.getValueAt(P_intROWID,TB1_FRMNO).toString();
						//System.out.println(tblTEPFM.getRowCount());
						for(int i=0;i<tblTEPFM.getRowCount();i++)
						if(tblTEPFM.getValueAt(i,TB1_FRMNO).toString().length()>0 && i!=P_intROWID)
						{
							if(newVAL.equals(tblTEPFM.getValueAt(i,TB1_FRMNO).toString())) 
							{
								setMSG("Form No Already Exist..",'E');
								return false;
							}
						}
						//System.out.println(">>>>"+intCNT1+">>>>"+P_intCOLID);
					}
					/*if(P_intCOLID==TB1_ISSDT)	
						{
							if(tblTEPFM.getValueAt(tblTEPFM.getSelectedRow(),TB1_ISSDT).toString().length()==0)
							{
								setMSG("Please Enter Issue Date..",'E');	
								return false;
							}	
						}*/
					/*if(P_intCOLID==TB1_EXPDT)	
						{
							if(tblTEPFM.getValueAt(tblTEPFM.getSelectedRow(),TB1_EXPDT).toString().length()==0)
							{	
								setMSG("Please Enter Expiery Date..",'E');	
								return false;
							}	
						}*/
					if(P_intCOLID==TB1_DLHDT)	
						{
							if(tblTEPFM.getValueAt(P_intROWID,P_intCOLID).toString().length() == 0)
								return true;
							if(M_fmtLCDAT.parse(txtDLHDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
							{
								setMSG("Date can not be greater than todays date..",'E');
								return false;
							}
							if(M_fmtLCDAT.parse(txtDLHDT.getText()).compareTo(M_fmtLCDAT.parse(txtEXPDT.getText())) > 0)
							{
								setMSG("Date can not be greater than expiery date..",'E');
								return false;
							}
						}	
					if(P_intCOLID==TB1_CMSDT)	
						{
							if(tblTEPFM.getValueAt(P_intROWID,P_intCOLID).toString().length() == 0)
								return true;
							if(M_fmtLCDAT.parse(txtCMSDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
							{
								setMSG("Date can not be greater than todays date..",'E');
								return false;
							}
							if(M_fmtLCDAT.parse(txtCMSDT.getText()).compareTo(M_fmtLCDAT.parse(txtEXPDT.getText())) > 0)
							{
								setMSG("Date can not be greater than Expiery date..",'E');
								return false;
							}
							
						}	
					if(P_intCOLID==TB1_NGTDT)	
						{
							if(tblTEPFM.getValueAt(P_intROWID,P_intCOLID).toString().length() == 0)
								return true;
							if(M_fmtLCDAT.parse(txtNGTDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
							{
								setMSG("Date can not be greater than todays date..",'E');
								return false;
							}
							if(M_fmtLCDAT.parse(txtNGTDT.getText()).compareTo(M_fmtLCDAT.parse(txtEXPDT.getText())) > 0)
							{
								setMSG("Date can not be greater than expiery date..",'E');
								return false;
							}
							
						}	
					
					/*if(tblTEPFM.getValueAt(tblTEPFM.getSelectedRow(),TB1_DLHDT).toString().length()==0)
						return true;
					if(tblTEPFM.getValueAt(tblTEPFM.getSelectedRow(),TB1_CMSDT).toString().length()==0)
						return true;
					if(tblTEPFM.getValueAt(tblTEPFM.getSelectedRow(),TB1_NGTDT).toString().length()==0)
						return true;*/
					
					
				}	
			}catch(Exception E_TL)
			{
				setMSG(E_TL,"Table Input Verifier");				
			}
			return true;
		}	
				
	}
	
	
	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
					return true;
				if(input==txtPRTTP)
				{
					M_strSQLQRY="Select distinct cmt_codcd from CO_CDTRN where  CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPRT' and";
					M_strSQLQRY+=" cmt_codcd ='"+txtPRTTP.getText().trim().toUpperCase()+"'";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						txtPRTTP.setText(txtPRTTP.getText().trim().toUpperCase());
					}	
					else 
					{
						setMSG("Enter Valid Party Type",'E');
						return false;
					}										
				}	
				if(input==txtPRTCD)
				{
					if(txtPRTCD.getText().length()< 5)
					{
						setMSG("Enter Valid Party Code",'E');
						return false;
					}	
					txtPRTCD.setText(txtPRTCD.getText().trim().toUpperCase());
					M_strSQLQRY="select PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP = '"+txtPRTTP.getText()+"' and  PT_PRTCD='"+txtPRTCD.getText()+"' ";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblPRTNM.setText(M_rstRSSET.getString("PT_PRTNM"));
					}	
					else 
					{
						setMSG("Enter Valid Party Code",'E');
						return false;
					}
				}	
				if(input==txtFRMTP)
				{
					if(txtFRMTP.getText().length() < 2)
						return true;				

					M_rstRSSET=null;
					M_strSQLQRY="select CMT_CODCD,CMT_CODDS from CO_CDTRN where  CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MRXXFTP' and  CMT_CODCD = '"+txtFRMTP.getText()+"'";
					M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblFRMDS.setText(M_rstRSSET.getString("CMT_CODDS"));
						//getDATA();
					}
					else
					{
						setMSG("Enter Valid Form Type",'E');
						return false;
					}	
				}	
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"Input Verifier");		
			}
			return true;
		}
	}	
}