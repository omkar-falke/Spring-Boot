import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.sql.*;
import java.util.StringTokenizer;
public class hr_rptgc extends cl_rbase implements ActionListener
{
	private TxtNumLimit txtEMPNO;
	private JTextField  txtEMPNM;
	private JTextField  txtEMPDP;
	private JTextField  txtEMPDG;
	private DataOutputStream L_doutREPORT;
	hr_rptgc()
	{
		super(2);
		add(new JLabel("Employe No"),2,1,1,1,this,'L');
		add(txtEMPNO=new TxtNumLimit(5.0),2,2,1,1,this,'L');
		add(new JLabel("Employe Name"),2,3,1,1,this,'L');
		add(txtEMPNM=new JTextField(),2,4,1,3,this,'L');
		add(new JLabel("Department"),3,1,1,1,this,'L');
		add(txtEMPDP=new JTextField(),3,2,1,1,this,'L');
		add(new JLabel("Designation"),3,3,1,1,this,'L');
		add(txtEMPDG=new JTextField(),3,4,1,3,this,'L');
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC==M_txtFMDAT)
			M_txtTODAT.requestFocus();
		else if(M_objSOURC==M_txtTODAT)
			txtEMPNO.requestFocus();
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC==txtEMPNO)
			setMSG("Enter Employee number, Press F1 for Emp. No. wise search, F2 for Name wise search ..",'N');
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(M_objSOURC==txtEMPNO)
		{
			if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strSQLQRY="select EP_EMPNO,EP_FULNM,EP_DPTNM,EP_DESGN from HR_EPMST where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' order by EP_EMPNO";
				M_strHLPFLD = "txtEMPNO";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Employee no.","Name","Department","Desgnation"},4,"CT");
			}
			else if(L_KE.getKeyCode()==L_KE.VK_F2)
			{
				M_strSQLQRY="select EP_FULNM,EP_EMPNO,EP_DPTNM,EP_DESGN from HR_EPMST where EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' order by EP_FULNM";
				M_strHLPFLD = "txtEMPNO1";
				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Name","Employee no.","Department","Desgnation"},4,"CT");
			}
		}
	}
	public void exeHLPOK()
	{
		try
		{
			if(M_strHLPFLD.equals("txtEMPNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtEMPNO.setText(L_STRTKN.nextToken());
				txtEMPNM.setText(L_STRTKN.nextToken());
				txtEMPDP.setText(L_STRTKN.nextToken());
				txtEMPDG.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtEMPNO1"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtEMPNM.setText(L_STRTKN.nextToken());
				txtEMPNO.setText(L_STRTKN.nextToken());
				txtEMPDP.setText(L_STRTKN.nextToken());
				txtEMPDG.setText(L_STRTKN.nextToken());
			}
		}catch(Exception L_EX)
		{
			System.out.println("Exception in exeHLPOK : "+L_EX);
		}
	}
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		txtEMPNM.setEnabled(false);txtEMPDP.setEnabled(false);txtEMPDG.setEnabled(false);
	}
	
	void exePRINT()
	{
		try
		{
			
			M_strSQLQRY="SELECT TG_FRMDT,TG_TITLE,TG_DURTN,TG_TGTYP,TGT_REMDS FROM HR_TGMST,HR_TGTRN WHERE HR_TGMST.TG_TRGCD=HR_TGTRN.TGT_TRNCD AND HR_TGMST.TG_CMPCD=HR_TGTRN.TGT_CMPCD AND TGT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TGT_EMPNO='"+txtEMPNO.getText().trim()+"' order by TG_FRMDT";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET!=null)
			{
				File filREPORT=new File("c:\\reports\\hr_rptgc.doc");
				FileOutputStream filOUT=new FileOutputStream(filREPORT);
				L_doutREPORT=new DataOutputStream(filOUT);
				M_intPAGNO=1;
				PRNHDR();
				int i=0;
				while(M_rstRSSET.next())
				{
					if(M_intLINNO>60)
					{
						prnFMTCHR(L_doutREPORT,M_strEJT);
						M_intPAGNO++;
						PRNHDR();
					}
					L_doutREPORT.writeBytes(padSTRING('R',Integer.toString(++i),6)
											+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("TG_FRMDT"),""),12)
											+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("TG_TITLE"),""),50)
											+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("TG_DURTN"),""),10)
											+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("TG_TGTYP"),""),10)
											+padSTRING('R',nvlSTRVL(M_rstRSSET.getString("TGT_REMDS"),""),20)+"\n");
					M_intLINNO++;
				}
				L_doutREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------\n");
			}
			else
			{
				setMSG("No training record found ..",'E');
				return;
			}
			prnFMTCHR(L_doutREPORT,M_strEJT);
			L_doutREPORT.flush();
			L_doutREPORT.close();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				doPRINT("c:\\reports\\hr_rptgc.doc");
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				Runtime r = Runtime.getRuntime();
				Process p = null;
				p  = r.exec("c:\\windows\\wordpad.exe "+" c:\\reports\\hr_rptgc.doc"); 
			}		
		}catch(Exception e)
		{setMSG(e,"Printing report");}
			
	}
	private void PRNHDR()
	{
		try
		{
			prnFMTCHR(L_doutREPORT,M_strBOLD);
			prnFMTCHR(L_doutREPORT,M_strCPI10);
			L_doutREPORT.writeBytes("SUPREME PETROCHEM LIMITED                                                                                             Page No - "+M_intPAGNO+"\n");
			L_doutREPORT.writeBytes("Individual Training Record \n");
			L_doutREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------\n\n\n");
			prnFMTCHR(L_doutREPORT,M_strNOBOLD);
			L_doutREPORT.writeBytes("Employee Name : "+padSTRING('R',txtEMPNM.getText(),30)+"Emp. No. : "+padSTRING('R',txtEMPNO.getText(),6)+"\n");
			L_doutREPORT.writeBytes("Department : "+padSTRING('R',txtEMPDP.getText(),36)+"Designation : "+padSTRING('R',txtEMPDG.getText(),6)+"\n");
			L_doutREPORT.writeBytes("For The Period : "+M_txtFMDAT.getText()+" To "+M_txtTODAT.getText()+" \n\n\n");
			prnFMTCHR(L_doutREPORT,M_strBOLD);
			prnFMTCHR(L_doutREPORT,M_strCPI17);
			prnFMTCHR(L_doutREPORT,M_strNOBOLD);
			L_doutREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------\n");
			L_doutREPORT.writeBytes(padSTRING('R',"Sr.No.",6)+padSTRING('R',"Date",12)+padSTRING('R',"Title",50)+padSTRING('R',"Duration",10)+padSTRING('R',"Type",10)+padSTRING('R',"Remarks",20));
			L_doutREPORT.writeBytes("\n");
			L_doutREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------\n");
			M_intLINNO=8;
		}catch (Exception e)
		{setMSG(e,"Printing Header");}
	}
	
}