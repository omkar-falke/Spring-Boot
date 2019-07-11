import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.Color;

import java.sql.SQLException;

class co_mepvn extends cl_pbase implements ChangeListener,MouseMotionListener{
	private JTabbedPane tbpMAIN;		//Declearing JTabbed Pane
	private JLabel lblPRTDT, lblMATDT;
	private JTextField txtPRTCD, txtPRTDS,txtPRTCP,txtMATCD,txtMATDS,txtUOMCD,txtCLSCD;
	private JPanel	pnlITEDT, pnlPRTDT;		//Panel for display two tables for paty and item  pnlPRTBL;
	private cl_JTBL tblPRTBL, tblMATBL,tblPRTDL,tblMATDL;				// JTable for party and material
	private String strSVAL1,strSVAL2;			
	private char chrTABSL;
	
	final int TBL_CHKFL = 0;
	
	final int TBL_PRTCD = 1;
	final int TBL_MATCD = 2;
	final int TBL_LPONO = 3;
	final int TBL_LPODT = 4;
	final int TBL_LPORT = 5;
	final int TBL_LPOVL = 6;
	final int TBL_CLSCD = 7;
	final int TBL_ENQNO = 8;
	final int TBL_ENQDT = 9;
	final int TBL_ENQQT = 10;
	final int TBL_QTNNO = 11;
	final int TBL_QTNDT = 12;
	final int TBL_QTNRT = 13;
	final int TBL_QTNVL = 14;
		
	co_mepvn(){
		super(1);
		tbpMAIN = new JTabbedPane();
		pnlITEDT = new JPanel(null);	// first tab panel
		pnlPRTDT = new JPanel(null);	// second tab panel
		txtPRTCD=new JTextField();
		txtPRTDS=new JTextField();
		txtPRTCP=new JTextField();
		txtMATCD=new JTextField();
		txtMATDS=new JTextField();
		txtUOMCD=new JTextField();
		txtCLSCD=new JTextField();
		lblPRTDT=new JLabel("Party Details");
		lblPRTDT.setForeground(Color.blue);
		lblMATDT=new JLabel("Item Details");
		lblMATDT.setForeground(Color.blue);
		
		setMatrix(20,6);
		add(tbpMAIN,1,1,18,9.6,this,'L');
		
		setMatrix(20,6);
		add(new JLabel("Material Code"),1,1,1,1,pnlITEDT,'L');
		add(txtMATCD,1,2,1,1,pnlITEDT,'L');
		add(new JLabel("Description"),2,1,1,1,pnlITEDT,'L');
		add(txtMATDS,2,2,1,3,pnlITEDT,'L');
		add(new JLabel("UOM"),3,1,1,1,pnlITEDT,'L');
		add(txtUOMCD,3,2,1,1,pnlITEDT,'L');
		add(lblPRTDT,4,1,1,1,pnlITEDT,'L');
		tblPRTBL=crtTBLPNL(pnlITEDT,new String[]{" ","Party Code","Description","Class","Capacity","Last P.O. No.","Rate","Enq No.","Qt. No."},300,5,1,8,6,new int[]{20,65,300,50,65,65,60,65,63},new int[]{0});
		tblPRTDL=crtTBLPNL(pnlITEDT,new String[]{"Last P.O. Dt","Quantity","Value","Enq Dt.","Enq Qty","Qt.Dt","Qt.Rt","Qt.Value"},5,14,1,3,6,new int[]{94,94,94,94,94,94,94,94},new int[]{});
		setMatrix(20,6);
		add(new JLabel("Party Code & Desc"),1,1,1,1,pnlPRTDT,'L');
		add(txtPRTCD,1,2,1,1,pnlPRTDT,'L');
		add(txtPRTDS,1,3,1,3,pnlPRTDT,'L');
		add(new JLabel("Party Class"),2,1,1,1,pnlPRTDT,'L');
		add(txtCLSCD,2,2,1,1,pnlPRTDT,'L');
		add(new JLabel("Capacity"),3,1,1,1,pnlPRTDT,'L');
		add(txtPRTCP,3,2,1,1,pnlPRTDT,'L');
		add(lblMATDT,4,1,1,1,pnlPRTDT,'L');
		tblMATBL=crtTBLPNL(pnlPRTDT,new String[]{" ","Material Code","Description","UOM","Last P.O. No.","Rate","Enq No.","Qt. No."},300,5,1,8,6,new int[]{20,75,335,65,65,65,65,63},new int[]{0});
		tblMATDL=crtTBLPNL(pnlPRTDT,new String[]{"Last P.O. Dt","Quantity","Value","Enq Dt.","Enq Qty","Qt.Dt","Qt.Rt","Qt.Value"},5,14,1,3,6,new int[]{94,94,94,94,94,94,94,94},new int[]{});
		
		tbpMAIN.addTab("Item wise",pnlITEDT);	// tabbed pan for itemwise
		tbpMAIN.addTab("Party wise",pnlPRTDT);	// tabbed pan for partywis
		
		tbpMAIN.addChangeListener(this);
		
		tblMATBL.addMouseListener(this);
		tblPRTBL.addMouseListener(this);
			
		setENBL(false);
		tbpMAIN.setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE){
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			clrCOMP();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)){
				
				tbpMAIN.setEnabled(true);
				
				txtPRTCD.setEnabled(true);
				txtMATCD.setEnabled(true);
				
				tblMATBL.setEnabled(true);
				tblMATDL.setEnabled(false);
				tblPRTBL.setEnabled(true);
				tblPRTDL.setEnabled(false);
				
				tbpMAIN.setSelectedIndex(0);
				txtMATCD.requestFocus();
				setMSG("Please Enter Material Code OR F1 For Help",'N');
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)){
				
				tbpMAIN.setEnabled(true);
				
				txtPRTCD.setEnabled(true);
				txtMATCD.setEnabled(true);
				
				tblMATBL.setEnabled(false);
				tblMATDL.setEnabled(false);
				tblPRTBL.setEnabled(false);
				tblPRTDL.setEnabled(false);
				
				tbpMAIN.setSelectedIndex(0);
				txtMATCD.requestFocus();
				tblMATBL.cmpEDITR[0].setEnabled(true);
				tblPRTBL.cmpEDITR[0].setEnabled(true);
				setMSG("Please Enter Material Code OR F1 For Help",'N');
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int key=L_KE.getKeyCode();
		
		if(L_KE.getSource()==(cl_dat.M_btnHLPOK_pbst)){	
			exeHLPOK();
		}

		if (L_KE.getKeyCode()==L_KE.VK_F1){
			if(M_objSOURC == txtMATCD){  //Help Window for Material code
				setCursor(cl_dat.M_curDFSTS_pbst);
				if(txtMATCD.getText().length()==0){
					M_strSQLQRY="SELECT CT_MATCD,CT_MATDS FROM CO_CTMST";
					M_strHLPFLD = "txtMATCD";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"MATERIAL CODE","MATERIAL DESCRIPTION"},2,"CT");
				}
				else{
					M_strSQLQRY="SELECT CT_MATCD, CT_MATDS, CT_UOMCD  FROM CO_CTMST WHERE CT_MATCD LIKE '"+txtMATCD.getText()+"%'"; 
					M_strHLPFLD = "txtMATCD";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"MATERIAL CODE","MATERIAL DESCRIPTION"},2,"CT");
				}
				setCursor(cl_dat.M_curWTSTS_pbst);
			}
		if(M_objSOURC == txtPRTCD)
			tblMATBL.clrTABLE();
		{
			if(txtPRTCD.getText().length()==0)
			{
				setMSG("Type First letter and then press F1",'E'); 
				return;
			}
			if(txtPRTCD.getText().length()==1)
			{	
				M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD from CO_PTMST where PT_PRTTP='S' and SUBSTRING(PT_PRTCD,1,1)= '"+txtPRTCD.getText().substring(0,1)+"' ORDER BY PT_PRTNM";
			}	
			else
				M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD from CO_PTMST where PT_PRTTP='S' and PT_PRTCD like '"+txtPRTCD.getText()+"%' ORDER BY PT_PRTNM";
				M_strHLPFLD = "txtPRTCD";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Supp. Name","Supp. Code"},2 ,"CT");
		}	
		}

		if(key==L_KE.VK_ENTER){  
			if(L_KE.getSource()==txtMATCD){	//method to show details in material wise table
				if (txtMATCD.getText().length()!=0){
					try{
						M_strSQLQRY="SELECT CTP_PRTCD,CTP_LPONO,CTP_LPORT,CTP_ENQNO,CTP_QTNNO,PT_PRTNM,PT_PURVL FROM CO_CTPTR,CO_PTMST WHERE PT_PRTTP='S' and CTP_PRTCD=PT_PRTCD AND CTP_MATCD='"+txtMATCD.getText()+"'";
						System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
						
						if(M_rstRSSET!=null){
							int i=0;
							while(M_rstRSSET.next()){
								tblPRTBL.setValueAt(M_rstRSSET.getString("CTP_PRTCD"),i,1);	//Party Code
								System.out.println("1");
								tblPRTBL.setValueAt(M_rstRSSET.getString("PT_PRTNM"),i,2);	//Party Name
								System.out.println("2");
			//					strAMINO = nvlSTRVL(M_rstRSSET.getString("in_amdno").toString(),"").trim();	
			//					strPORNO = nvlSTRVL(M_rstRSSET.getString("in_porno"),"").trim();	
								//tblPRTBL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTP_CLSCD").toString(),""),i,3);	//Party Class
								System.out.println("3");
								//tblPRTBL.setValueAt(M_rstRSSET.getString("PT_PURVL"),i,4);	//Party Capacity
								System.out.println("4");
								tblPRTBL.setValueAt(M_rstRSSET.getString("CTP_LPONO"),i,5);	//Party Last P.O. No.
								tblPRTBL.setValueAt(M_rstRSSET.getString("CTP_LPORT"),i,6);	//Party Rate
								//tblPRTBL.setValueAt(M_rstRSSET.getString("CTP_ENQNO"),i,7);	//Enqury No.
								//tblPRTBL.setValueAt(M_rstRSSET.getString("CTP_QTNNO"),i,8);	//Quotation No.
								i++;
							}
						}
						setMSG("Click On CheckBox To View Details ",'N');
					}
					catch(SQLException L_SQLE){
						System.out.println("Database not found");
						setMSG("Record Not Found ",'E');
					}
				}
			}
			
			if(L_KE.getSource()==txtPRTCD){	//method to show details in material wise table
				if (txtPRTCD.getText().length()!=0){
					try{
						M_strSQLQRY="SELECT CTP_MATCD,CTP_LPONO,CTP_LPORT,CTP_ENQNO,CTP_QTNNO,CT_MATDS,CT_UOMCD FROM CO_CTPTR,CO_CTMST WHERE CT_MATCD=CTP_MATCD AND CTP_PRTCD='"+txtPRTCD.getText()+"' order by CT_MATCD";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
						if(M_rstRSSET!=null){
							int i=0;
							while(M_rstRSSET.next()){
								tblMATBL.setValueAt(M_rstRSSET.getString("CTP_MATCD"),i,1); //Material Code
								tblMATBL.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,2);	//Material Descriptin
								tblMATBL.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,3);	//Unit of Measurment
								tblMATBL.setValueAt(M_rstRSSET.getString("CTP_LPONO"),i,4);	//Last P.O. No.
								tblMATBL.setValueAt(M_rstRSSET.getString("CTP_LPORT"),i,5);	//Rate
								tblMATBL.setValueAt(M_rstRSSET.getString("CTP_ENQNO"),i,6);	//Enquary No.
								tblMATBL.setValueAt(M_rstRSSET.getString("CTP_QTNNO"),i,7);	//Quotation No.
								i++;
							}
						}
						setMSG("Click On CheckBox To View Details ",'N');
					}
					catch(SQLException L_SQLE){
						System.out.println("Database not found");
						setMSG("Record Not Found ",'E');
					}
				}
			}
		}
	}
	public void keyTyped(KeyEvent L_KE)
	{
	}
	public void keyReleased(KeyEvent L_KE)
	{
	}
	
	public void stateChanged(ChangeEvent L_CE)		//FOR TABBED PANE SELECTION
	{
		if (tbpMAIN.getSelectedIndex()==0){
			chrTABSL='M';							//For Material wise tabbed pan
			txtMATCD.requestFocus();
			setMSG("Please Enter Material Code OR F1 For Help",'N');
		}
		else if (tbpMAIN.getSelectedIndex()==1){
			chrTABSL='P';							//For Party wise tabbed pan
			txtPRTCD.requestFocus();
			setMSG("Please Enter Party Code OR F1 For Help",'N');
		}
	}
	public void mouseEntered(MouseEvent L_ME){
	}
	public void mouseDragged(MouseEvent L_ME){
	}
	public void mouseExited(MouseEvent L_ME){
	}
	public void mousePressed(MouseEvent L_ME){
	}
	public void mouseMoved(MouseEvent L_ME){
	}
	public void mouseClicked(MouseEvent L_ME){
		super.mouseClicked(L_ME);
		if(L_ME.getSource() == tblMATBL){
			if(tblPRTBL.getSelectedColumn() == 0){
				int L_intSELROW = tblMATBL.getSelectedRow();
				for(int i=0;i<tblMATBL.getRowCount();i++){
					if(i != L_intSELROW)
						tblMATBL.setValueAt(new Boolean(false),i,0);
				}
				try{
					tblMATDL.clrTABLE();
					M_strSQLQRY="Select * from CO_CTPTR where ctp_matcd='"+tblMATBL.getValueAt(L_intSELROW,1).toString()+"' and CTP_PRTCD='"+txtPRTCD.getText()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					M_rstRSSET.next();
					tblMATDL.setValueAt(M_rstRSSET.getString("CTP_LPODT"),0,0);
					tblMATDL.setValueAt(M_rstRSSET.getString("CTP_LPOQT"),0,1);
					tblMATDL.setValueAt(M_rstRSSET.getString("CTP_LPOVL"),0,2);
					tblMATDL.setValueAt(M_rstRSSET.getString("CTP_ENQDT"),0,3);
					tblMATDL.setValueAt(M_rstRSSET.getString("CTP_ENQQT"),0,4);
					tblMATDL.setValueAt(M_rstRSSET.getString("CTP_QTNDT"),0,5);
					tblMATDL.setValueAt(M_rstRSSET.getString("CTP_QTNRT"),0,6);
					tblMATDL.setValueAt(M_rstRSSET.getString("CTP_QTNVL"),0,7);
				}
				catch(SQLException L_SQLE){
					System.out.println("Database not found");
					setMSG("MATCHING VALUE NOT FOUND",'N');
				}
			}
		}
		if(L_ME.getSource() == tblPRTBL){
			System.out.println("tblPRTBL");
			if(tblMATBL.getSelectedColumn() == 0){
				int L_intSELROW = tblPRTBL.getSelectedRow();
				for(int i=0;i<tblPRTBL.getRowCount();i++){
					if(i != L_intSELROW)
						tblPRTBL.setValueAt(new Boolean(false),i,0);
				}
				try{
					tblPRTDL.clrTABLE();
					System.out.println(txtPRTCD.getText());
					M_strSQLQRY="Select * from co_ctptr where CTP_MATCD='"+tblPRTBL.getValueAt(L_intSELROW,1).toString()+"' and CTP_PRTCD='"+txtPRTCD.getText()+"'";
					System.out.println(M_strSQLQRY); 
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					M_rstRSSET.next();
					tblPRTDL.setValueAt(M_rstRSSET.getString("CTP_LPODT"),0,0);
					tblPRTDL.setValueAt(M_rstRSSET.getString("CTP_LPOQT"),0,1);
					tblPRTDL.setValueAt(M_rstRSSET.getString("CTP_LPOVL"),0,2);
					tblPRTDL.setValueAt(M_rstRSSET.getString("CTP_ENQDT"),0,3);
					tblPRTDL.setValueAt(M_rstRSSET.getString("CTP_ENQQT"),0,4);
					tblPRTDL.setValueAt(M_rstRSSET.getString("CTP_QTNDT"),0,5);
					tblPRTDL.setValueAt(M_rstRSSET.getString("CTP_QTNRT"),0,6);
					tblPRTDL.setValueAt(M_rstRSSET.getString("CTP_QTNVL"),0,7);
				}
				catch(SQLException L_SQLE){
					System.out.println("Database not found");
					setMSG("MATCHING VALUE NOT FOUND",'N');
				}
			}
		}
	}

	public void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			cl_dat.M_wndHLP_pbst=null;
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtPRTCD"))
			{
				//System.out.println(cl_dat.M_strHELP_pbst);
				txtPRTDS.setText(L_STRTKN.nextToken());
				txtPRTCD.setText(L_STRTKN.nextToken());
				//L_STRTKN.nextToken();
				//strBYRDT=txtBYRNM.getText();
				//while (L_STRTKN.hasMoreTokens())
				//	strBYRDT += "\n"+L_STRTKN.nextToken();
				//txtCNSCD.setText(txtBYRCD.getText());
				//txtCNSNM.setText(txtBYRNM.getText());
				txtPRTCD.requestFocus();
				setMSG("Press Enter To View Details",'N');
			}
		if(M_strHLPFLD.equals("txtMATCD"))
		{
			txtMATCD.setText(L_STRTKN.nextToken());
			txtMATDS.setText(L_STRTKN.nextToken());
			M_strSQLQRY="SELECT CT_MATDS,CT_UOMCD FROM CO_CTMST WHERE CT_MATCD='"+txtMATCD.getText()+"'";
			System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
			M_rstRSSET.next();
			txtMATDS.setText(M_rstRSSET.getString("CT_MATDS"));
			txtUOMCD.setText(M_rstRSSET.getString("CT_UOMCD"));
			txtMATCD.requestFocus();
			setMSG("Press Enter To View Details",'N');
		}
		}	
		catch(Exception L_EX)
		{	
			setMSG(L_EX,"in Child.exeHLPOK");
		}
	}	
	
	void clrCOMP(){
		txtPRTCD.setText("");
		txtPRTDS.setText("");
		txtPRTCP.setText("");
		txtMATCD.setText("");
		txtMATDS.setText("");
		txtUOMCD.setText("");
		txtCLSCD.setText("");
		tblPRTBL.clrTABLE();
		tblMATBL.clrTABLE();
		tblPRTDL.clrTABLE();
		tblMATDL.clrTABLE();
	}
}