
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
/**
<P>
<TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" 
width="75%">
  
  <TR>
    <TD>System Name</TD>
    <TD>Human Resource Management System </TD></TR>
  <TR>
    <TD>Program Desc</TD>
    <TD>Entry form for Grade Master </TD></TR>
  <TR>
    <TD>Author </TD>
    <TD>AAP </TD></TR>
  <TR>
    <TD>Date</TD>
    <TD>20/03/2003 </TD></TR>
  <TR>
    <TD>Version </TD>
    <TD>2.0.0</TD></TR></TABLE></P>
<P>&nbsp;</P>
<P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P>
<P>
<TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 58px; WIDTH: 765px" 
width="100%">
  
  <TR>
    <TD>
      <P align=center>Table Name</P></TD>
    <TD>
      <P align=center>Primary Key</P></TD>
    <TD>
      <P align=center>Add</P></TD>
    <TD>
      <P align=center>Mod</P></TD>
    <TD>
      <P align=center>Del</P></TD>
    <TD>
      <P align=center>Enq</P></TD></TR>
  <TR>
    <TD>HR_GRMST</TD>
    <TD>GR_GRDCD</TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center><FONT 
face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>
*/
class hr_megrm extends cl_pbase
{
	TxtNumLimit txtSPALW,
				txtLNSUB,
				txtCONVY,
				txtCHEDN,
				txtMDALW,
				txtLTALW,
				txtPFALW,
				txtSAALW,
				txtHRALW,
				txtDNALW,
				txtWKALW;
	TxtLimit	txtGRDCD;
	
	hr_megrm()
	{
		super(1);
		txtSPALW=new TxtNumLimit(7.2);txtLNSUB=new TxtNumLimit(7.2);txtCONVY=new TxtNumLimit(7.2);
		txtCHEDN=new TxtNumLimit(7.2);txtMDALW=new TxtNumLimit(7.2);txtLTALW=new TxtNumLimit(7.2);
		txtPFALW=new TxtNumLimit(7.2);txtSAALW=new TxtNumLimit(7.2);txtHRALW=new TxtNumLimit(7.2);
		txtDNALW=new TxtNumLimit(7.2);txtWKALW=new TxtNumLimit(7.2);txtGRDCD=new TxtLimit(6);
	
		setMatrix(20,4);
		add(new JLabel("Grade Code : "),1,1,1,1,this,'L');
		add(txtGRDCD,1,2,1,1,this,'L');
		add(new JLabel("Special Allowance : "),2,1,1,1,this,'L');
		add(txtSPALW,2,2,1,1,this,'L');
		add(new JLabel("Lunch Subcidy : "),2,3,1,1,this,'L');
		add(txtLNSUB,2,4,1,1,this,'L');
		add(new JLabel("Conveyance : "),3,1,1,1,this,'L');
		add(txtCONVY,3,2,1,1,this,'L');
		add(new JLabel("Child Education Allowance : "),3,3,1,1,this,'L');
		add(txtCHEDN,3,4,1,1,this,'L');
		add(new JLabel("Medical Allowance : "),4,1,1,1,this,'L');
		add(txtMDALW,4,2,1,1,this,'L');
		add(new JLabel("L. T. A. (Annual, % of cons. basic) : "),4,3,1,1,this,'L');
		add(txtLTALW,4,4,1,1,this,'L');
		add(new JLabel("Providend Fund : "),5,1,1,1,this,'L');
		add(txtPFALW,5,2,1,1,this,'L');
		add(new JLabel("Super Annuation : "),5,3,1,1,this,'L');
		add(txtSAALW,5,4,1,1,this,'L');
		add(new JLabel("House Rent Allowance : "),6,1,1,1,this,'L');
		add(txtHRALW,6,2,1,1,this,'L');
		add(new JLabel("Dearness Assistance : "),6,3,1,1,this,'L');
		add(txtDNALW,6,4,1,1,this,'L');
		add(new JLabel("Work Allowance : "),7,1,1,1,this,'L');
		add(txtWKALW,7,2,1,1,this,'L');
	}

	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		Object L_SRC=L_AE.getSource();
		String L_STRTMP=cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString();
		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
		{
			if(!L_STRTMP.equals(cl_dat.M_OPSEL_pbst))
			{
				txtGRDCD.setEnabled(true);
				txtGRDCD.requestFocus();
				txtGRDCD.setText("");
			}
		}
		if(L_SRC==cl_dat.M_btnSAVE_pbst)
		{
			exeADDREC();
		}
		else if(L_SRC==txtGRDCD)
		{
			if(L_STRTMP.equals(cl_dat.M_OPENQ_pbst))
			{
				exeSELREC();
			}
			else if(L_STRTMP.equals(cl_dat.M_OPMOD_pbst))
			{
				exeSELREC();
				txtGRDCD.setEnabled(false);
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		String L_STRTMP=cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString();
		if (L_KE.getKeyCode()==L_KE.VK_F1)
		{
			 if(L_KE.getSource()==txtGRDCD)
			 {
				 M_strSQLQRY="SELECT GR_GRDCD FROM HR_GRMST ORDER BY GR_GRDCD";
				 M_strHLPFLD = "txtGRDCD";
				 cl_hlp(M_strSQLQRY ,1,1,new String[] {"Grade Code."},1,"CT");
			 }
		}
	}
	private boolean vldDAT()
	{
		if(txtGRDCD.getText()=="")
		{
			txtGRDCD.requestFocus();
			return false;
		}
		else
			txtGRDCD.setText(txtGRDCD.getText().toUpperCase());
		if(txtLNSUB.getText()=="0.00"||txtLNSUB.getText()=="")
		{
			txtLNSUB.requestFocus();
			return false;
		}
		if(txtPFALW.getText()==""||txtPFALW.getText()=="0.00")
		{
			txtPFALW.requestFocus();
			return false;
		}
		if(txtHRALW.getText()==""||txtHRALW.getText()=="0.00")
		{
			txtHRALW.requestFocus();
			return false;
		}
		if(txtLTALW.getText()==""||txtHRALW.getText().length()<5)
		{
			txtLTALW.requestFocus();
			return false;
		}
		return true;
	}

	private void exeSELREC()
	{
		try
		{
			M_strSQLQRY="SELECT * FROM HR_GRMST WHERE GR_GRDCD='"+txtGRDCD.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET.next())
			{
				txtSPALW.setText(nvlSTRVL(M_rstRSSET.getString("GR_SPALW"),""));
				txtLNSUB.setText(nvlSTRVL(M_rstRSSET.getString("GR_LNSUB"),""));
				txtCONVY.setText(nvlSTRVL(M_rstRSSET.getString("GR_CONVY"),""));
				txtCHEDN.setText(nvlSTRVL(M_rstRSSET.getString("GR_CHEDN"),""));
				txtMDALW.setText(nvlSTRVL(M_rstRSSET.getString("GR_MDALW"),""));
				txtLTALW.setText(nvlSTRVL(M_rstRSSET.getString("GR_LTALW"),""));
				txtPFALW.setText(nvlSTRVL(M_rstRSSET.getString("GR_PFALW"),""));
				txtSAALW.setText(nvlSTRVL(M_rstRSSET.getString("GR_SAALW"),""));
				txtHRALW.setText(nvlSTRVL(M_rstRSSET.getString("GR_HRALW"),""));
				txtDNALW.setText(nvlSTRVL(M_rstRSSET.getString("GR_DNALW"),""));
				txtWKALW.setText(nvlSTRVL(M_rstRSSET.getString("GR_WKALW"),""));
				cl_dat.M_txtUSER_pbst.setText(M_rstRSSET.getString("GR_LUSBY"));
				cl_dat.M_txtDATE_pbst.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_LUPDT")));
			}
		}
		catch(Exception L_E)
		{
			System.out.println(L_E);
			setMSG("Grade not found ..",'E');
		}
	}
	
	private void exeADDREC()
	{
		if(vldDAT())
		{
			try
			{
				if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPADD_pbst))
				{
					M_strSQLQRY="INSERT INTO HR_GRMST (GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,GR_WKALW,GR_TRNFL,GR_STSFL,gr_LUSBY,GR_LUPDT) values ("
						+"'"+txtGRDCD.getText().trim()+"',"
						+nvlSTRVL(txtSPALW.getText().trim(),"0")+","
						+nvlSTRVL(txtLNSUB.getText().trim(),"0")+","
						+nvlSTRVL(txtCONVY.getText().trim(),"0")+","
						+nvlSTRVL(txtCHEDN.getText().trim(),"0")+","
						+nvlSTRVL(txtMDALW.getText().trim(),"0")+","
						+nvlSTRVL(txtLTALW.getText().trim(),"0")+","
						+nvlSTRVL(txtPFALW.getText().trim(),"0")+","
						+nvlSTRVL(txtSAALW.getText().trim(),"0")+","
						+nvlSTRVL(txtHRALW.getText().trim(),"0")+","
						+nvlSTRVL(txtDNALW.getText().trim(),"0")+","
						+nvlSTRVL(txtWKALW.getText().trim(),"0")+","
						+getUSGDTL("GR",'i')+")";//TG_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPMOD_pbst)&&cl_dat.M_flgLCUPD_pbst)
				{
					M_strSQLQRY="UPDATE HR_GRMST SET "
						+"GR_SPALW = "+nvlSTRVL(txtSPALW.getText().trim(),"0")+","
						+"GR_LNSUB = "+nvlSTRVL(txtLNSUB.getText().trim(),"0")+","
						+"GR_CONVY = "+nvlSTRVL(txtCONVY.getText().trim(),"0")+","
						+"GR_CHEDN = "+nvlSTRVL(txtCHEDN.getText().trim(),"0")+","
						+"GR_MDALW = "+nvlSTRVL(txtMDALW.getText().trim(),"0")+","
						+"GR_LTALW = "+nvlSTRVL(txtLTALW.getText().trim(),"0")+","
						+"GR_PFALW = "+nvlSTRVL(txtPFALW.getText().trim(),"0")+","
						+"GR_SAALW = "+nvlSTRVL(txtSAALW.getText().trim(),"0")+","
						+"GR_HRALW = "+nvlSTRVL(txtHRALW.getText().trim(),"0")+","
						+"GR_DNALW = "+nvlSTRVL(txtDNALW.getText().trim(),"0")+","
						+"GR_WKALW = "+nvlSTRVL(txtWKALW.getText().trim(),"0")+","
						+getUSGDTL("GR",'u')+" where GR_GRDCD='"+txtGRDCD.getText().trim()+"'";//TG_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				if(cl_dat.M_flgLCUPD_pbst)
				{
					M_strSQLQRY="INSERT INTO HR_GRTRN (GRT_GRDCD,GRT_SPALW,GRT_LNSUB,GRT_CONVY,GRT_CHEDN,GRT_MDALW,GRT_LTALW,GRT_PFALW,GRT_SAALW,GRT_HRALW,GRT_DNALW,GRT_WKALW,GRT_TRNFL,GRT_STSFL,grT_LUSBY,GRT_LUPDT) values ("
						+"'"+nvlSTRVL(txtGRDCD.getText().trim(),"0")+"',"
						+nvlSTRVL(txtSPALW.getText().trim(),"0")+","
						+nvlSTRVL(txtLNSUB.getText().trim(),"0")+","
						+nvlSTRVL(txtCONVY.getText().trim(),"0")+","
						+nvlSTRVL(txtCHEDN.getText().trim(),"0")+","
						+nvlSTRVL(txtMDALW.getText().trim(),"0")+","
						+nvlSTRVL(txtLTALW.getText().trim(),"0")+","
						+nvlSTRVL(txtPFALW.getText().trim(),"0")+","
						+nvlSTRVL(txtSAALW.getText().trim(),"0")+","
						+nvlSTRVL(txtHRALW.getText().trim(),"0")+","
						+nvlSTRVL(txtDNALW.getText().trim(),"0")+","
						+nvlSTRVL(txtWKALW.getText().trim(),"0")+","
						+getUSGDTL("GRT",'i')+")";//TG_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					if(!cl_dat.exeDBCMT("exeSAVE"))
						setMSG("Modification Completed..",'N');
					else
						setMSG("Modification Failed..",'E');
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			catch(Exception L_E)
			{
				cl_dat.M_flgLCUPD_pbst=false;
				cl_dat.exeDBCMT("exeSAVE");
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				showEXMSG(L_E,"exeADDREC","");
			}
		}
	}
	public void exeHLPOK()
	{
		try
		{
			if(M_strHLPFLD.equals("txtGRDCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				txtGRDCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}
		catch(Exception L_EX)
		{
			System.out.println("Exception in exeHLPOK : "+L_EX);
		}
	}
}