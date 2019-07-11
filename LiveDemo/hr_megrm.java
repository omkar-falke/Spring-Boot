
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

/**
<P><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%">    <TR>    <TD>System Name</TD>    <TD>Human Resource Management System </TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>Entry form for Grade Master </TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>20/03/2003 </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR></TABLE></P><P>&nbsp;</P><P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P><P><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 765px; HEIGHT: 58px" width="100%">    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>HR_GRMST</TD>    <TD>GR_GRDCD</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>HR_GRMAM</TD>    <TD>GR_GRDCD,GR_YRDGT</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P><P>&nbsp;</P><P><STRONG><FONT color=purple>Remarks : </FONT></STRONG></P><UL>  <LI>Modification of grade is not allowed for next finantial year.</LI>  <LI>If modification is back-dated for earlier finantial year i.e. log date is   less than start date of current finantial year, Modification is recorded in   HR_GRMAM only.</LI>  <LI>If modification is for current finantial year, data is modified in   HR_GRMST and original values before modification are copied to HR_GRMAM with   curren finantial year digit.</LI></UL>*/
class hr_megrm extends cl_pbase
{
	/**	Special Allowance */
	TxtNumLimit				txtSPALW;/** Lunch Subsidy */
	TxtNumLimit 			txtLNSUB;/** Conveyance */
	TxtNumLimit 			txtCONVY;/** Child Education Allowance */
	TxtNumLimit 			txtCHEDN;/** Medical Allowance */
	TxtNumLimit 			txtMDALW;/** LTA */
	TxtNumLimit 			txtLTALW;/** Car Allow */
	TxtNumLimit 			txtCRALW;/** Grade Allow */
	TxtNumLimit 			txtGRALW;/** PF */
	TxtNumLimit 			txtPFALW;/** Super Ammuation */
	TxtNumLimit 			txtSAALW;/** House Rent Allowance */
	TxtNumLimit 			txtHRALW;/** Dearness Allowance */
	TxtNumLimit 			txtDNALW;/** Work Allowance */
	TxtNumLimit 			txtWKALW;/** Grade Code */
	TxtLimit	            txtGRDCD;/** Washing Allowance */
    TxtNumLimit 			txtWSALW;
	private JCheckBox chkMDAFL;
	private JCheckBox chkLTAFL;
	private INPVF oINPVF;
	/** To Construct the screen */
	hr_megrm()
	{
		super(2);
		setMatrix(20,20);
		add(new JLabel("Grade Code : "),1,1,1,4,this,'L');
		add(txtGRDCD=new TxtLimit(6),1,5,1,3,this,'L');
		
		add(new JLabel("Special Allowance : "),2,1,1,4,this,'L');
		add(txtSPALW=new TxtNumLimit(7.2),2,5,1,3,this,'L');
		add(new JLabel("Lunch Subcidy : "),2,10,1,4,this,'L');
		add(txtLNSUB=new TxtNumLimit(7.2),2,14,1,3,this,'L');
		
		add(new JLabel("Conveyance : "),3,1,1,4,this,'L');
		add(txtCONVY=new TxtNumLimit(7.2),3,5,1,3,this,'L');
		add(new JLabel("Child Education Allowance : "),3,10,1,4,this,'L');
		add(txtCHEDN=new TxtNumLimit(7.2),3,14,1,3,this,'L');
		
		add(new JLabel("Medical Allow: "),4,1,1,2,this,'L');
		add(chkMDAFL=new JCheckBox("% Wise", false),4,3,1,2,this,'L');
		add(txtMDALW=new TxtNumLimit(7.2),4,5,1,3,this,'L');
		add(new JLabel("L.T.A. : "),4,10,1,2,this,'L');
		add(chkLTAFL=new JCheckBox("% Wise", false),4,12,1,2,this,'L');		
		add(txtLTALW=new TxtNumLimit(7.2),4,14,1,3,this,'L');
		
		add(new JLabel("P.F. (%of Basic) : "),5,1,1,4,this,'L');
		add(txtPFALW=new TxtNumLimit(7.2),5,5,1,3,this,'L');
		add(new JLabel("S.Annu.(%of C.Basic) : "),5,10,1,4,this,'L');
		add(txtSAALW=new TxtNumLimit(7.2),5,14,1,3,this,'L');
		
		add(new JLabel("H.R.A."),6,1,1,2,this,'L');
		add(txtHRALW=new TxtNumLimit(7.2),6,5,1,3,this,'L');
		add(new JLabel("Dearness Allowance : "),6,10,1,4,this,'L');
		add(txtDNALW=new TxtNumLimit(7.2),6,14,1,3,this,'L');
		
		add(new JLabel("Work Allowance : "),7,1,1,4,this,'L');
		add(txtWKALW=new TxtNumLimit(7.2),7,5,1,3,this,'L');
        add(new JLabel("Washing Allowance : "),7,10,1,4,this,'L');
        add(txtWSALW=new TxtNumLimit(7.2),7,14,1,3,this,'L');
		
		add(new JLabel("Grade Allowance : "),8,1,1,4,this,'L');
		add(txtGRALW=new TxtNumLimit(7.2),8,5,1,3,this,'L');
		add(new JLabel("Car Allowance : "),8,10,1,4,this,'L');
		add(txtCRALW=new TxtNumLimit(7.2),8,14,1,3,this,'L');
		oINPVF=new INPVF();
		txtGRDCD.setInputVerifier(oINPVF);
	}
/**<b>TASKS : </b>br
 * &nbsp&nbsp&nbsp&nbspcmbOPTN : State change of components and focus transfer.
 * &nbsp&nbsp&nbsp&nbspbtnSAVE : CALL TO RESP. METHODS
 * &nbsp&nbsp&nbsp&nbsptxtGRDCD : Retrieve grade details in case of Modification and Enquirey
 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		String L_STRTMP=cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString();
		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
		{
			if(!L_STRTMP.equals(cl_dat.M_OPSEL_pbst))
			{
				chkMDAFL.setSelected(false);
				txtGRDCD.setEnabled(true);
				txtGRDCD.requestFocus();
				txtGRDCD.setText("");
			}
		}
		else if(M_objSOURC==txtGRDCD)
		{
			if(L_STRTMP.equals(cl_dat.M_OPENQ_pbst))
				exeSELREC();
			else if(L_STRTMP.equals(cl_dat.M_OPMOD_pbst))
			{
				exeSELREC();
				txtGRDCD.setEnabled(false);
			}
		}
	}
	/** Help on Grade COde	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode()==L_KE.VK_F1)
		{
			 if(L_KE.getSource()==txtGRDCD)
			 {
				 M_strSQLQRY="select cmt_codcd from co_cdtrn where cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HRXXGRD'";
				 if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					M_strSQLQRY+=" and cmt_codcd not in (select GR_GRDCD from HR_GRMST where GR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"')";
    			 else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					M_strSQLQRY+=" and cmt_codcd in(select GR_GRDCD from HR_GRMST where GR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"')";
				 System.out.println("f1>>"+M_strSQLQRY);
				 M_strHLPFLD = "txtGRDCD";
				 cl_hlp(M_strSQLQRY ,1,1,new String[] {"Grade Code."},1,"CT");
			 }
		}
		if(L_KE.getKeyCode()==L_KE.VK_ENTER)
		{
			 if(L_KE.getSource()==txtGRDCD)
				txtSPALW.requestFocus();
		}
	}
	boolean vldDATA()
	{
		if(txtGRDCD.getText()=="")
		{
			txtGRDCD.requestFocus();
			setMSG("Please enter Grade Code ..",'E');
			return false;
		}
		else
			txtGRDCD.setText(txtGRDCD.getText().toUpperCase());
		if(txtLNSUB.getText()=="0.00"||txtLNSUB.getText()=="")
		{
			txtLNSUB.requestFocus();
			setMSG("Please enter Lunch Subsidy ..",'E');
			return false;
		}
		if(txtPFALW.getText()==""||txtPFALW.getText()=="0.00")
		{
			txtPFALW.requestFocus();
			setMSG("Please enter P.F. ..",'E');
			return false;
		}
		if(txtHRALW.getText()==""||txtHRALW.getText()=="0.00")
		{
			txtHRALW.requestFocus();
			setMSG("Please enter H.R.A. ..",'E');
			return false;
		}
		// Modified on 20/10/2005 to enter GET gardes
		//if(txtLTALW.getText()==""||txtHRALW.getText().length()<5)
		if(txtLTALW.getText()=="")
        {
			txtLTALW.requestFocus();
			setMSG("Please enter L.T.A. ..",'E');
			return false;
		}
		return true;
	}
/**To retrieve grade details  */
	private void exeSELREC()
	{
		try
		{
			M_strSQLQRY="SELECT * FROM HR_GRMST WHERE GR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and GR_GRDCD='"+txtGRDCD.getText().trim()+"'";
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
				txtWSALW.setText(nvlSTRVL(M_rstRSSET.getString("GR_WSALW"),""));
				txtGRALW.setText(nvlSTRVL(M_rstRSSET.getString("GR_GRALW"),""));
				txtCRALW.setText(nvlSTRVL(M_rstRSSET.getString("GR_CRALW"),""));
				if(nvlSTRVL(M_rstRSSET.getString("GR_MDAFL"),"").equals("P"))
					chkMDAFL.setSelected(true);
				else 
					chkMDAFL.setSelected(false);
				if(nvlSTRVL(M_rstRSSET.getString("GR_LTAFL"),"").equals("P"))
					chkLTAFL.setSelected(true);
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
	/**To insert & Modify record.<br> also makes entry to ammendment table	 */
	void exeSAVE()
	{
		if(vldDATA())
		{
			try
			{
				if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPADD_pbst))
				{
					M_strSQLQRY="INSERT INTO HR_GRMST (GR_SBSCD,GR_CMPCD,GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_MDAFL,GR_LTALW,GR_LTAFL,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_GRALW,GR_CRALW,GR_TRNFL,GR_STSFL,gr_LUSBY,GR_LUPDT) values (";
					M_strSQLQRY+="'"+M_strSBSCD+"',";
						M_strSQLQRY+="'"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY+="'"+txtGRDCD.getText().trim()+"',";
						M_strSQLQRY+=nvlSTRVL(txtSPALW.getText().trim(),"0")+",";
						M_strSQLQRY+=nvlSTRVL(txtLNSUB.getText().trim(),"0")+",";
						M_strSQLQRY+=nvlSTRVL(txtCONVY.getText().trim(),"0")+",";
						M_strSQLQRY+=nvlSTRVL(txtCHEDN.getText().trim(),"0")+",";
						M_strSQLQRY+=nvlSTRVL(txtMDALW.getText().trim(),"0")+",";
						M_strSQLQRY+=chkMDAFL.isSelected()? "'P'":"'A'";
						M_strSQLQRY+=nvlSTRVL(txtLTALW.getText().trim(),"0")+",";
						M_strSQLQRY+=chkLTAFL.isSelected()? "'P'":"'A'";
						M_strSQLQRY+=nvlSTRVL(txtPFALW.getText().trim(),"0")+",";
						M_strSQLQRY+=nvlSTRVL(txtSAALW.getText().trim(),"0")+",";
						M_strSQLQRY+=nvlSTRVL(txtHRALW.getText().trim(),"0")+",";
						M_strSQLQRY+=nvlSTRVL(txtDNALW.getText().trim(),"0")+",";
						M_strSQLQRY+=nvlSTRVL(txtWKALW.getText().trim(),"0")+",";
						M_strSQLQRY+=nvlSTRVL(txtWSALW.getText().trim(),"0")+",";
						M_strSQLQRY+=nvlSTRVL(txtGRALW.getText().trim(),"0")+",";
						M_strSQLQRY+=nvlSTRVL(txtCRALW.getText().trim(),"0")+",";
						M_strSQLQRY+=getUSGDTL("GR",'I',"0")+")";//TG_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPMOD_pbst)&&cl_dat.M_flgLCUPD_pbst)
				{
					System.out.println(1);
					boolean L_flgOLDDT=false;
					cl_dat.M_flgLCUPD_pbst = true;
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					String L_strYRDGT=Integer.toString( M_calLOCAL.get(M_calLOCAL.YEAR));
					if(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(cl_dat.M_strYSTDT_pbst))<0)
					{
					    JOptionPane.showMessageDialog(this, "Please check the login date ..", "Error", JOptionPane.ERROR_MESSAGE) ;
					    return;
					    // COMMENTED TEMPORARILY ON 02/05/2005
						/*
						cl_dat.exeSQLUPD("Insert into HR_GRMAM (GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_TRNFL,GR_STSFL,gr_LUSBY,GR_LUPDT,GR_YRDGT) select GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_TRNFL,GR_STSFL,gr_LUSBY,GR_LUPDT,"+L_strYRDGT+" from HR_GRMST where GR_SBSCD='"+M_strSBSCD+"' and GR_GRDCD='"+txtGRDCD.getText()+"'","setLCLPD");
						L_flgOLDDT=true;
						System.out.println("2 : Insert into HR_GRMAM (GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_TRNFL,GR_STSFL,gr_LUSBY,GR_LUPDT,GR_YRDGT) select GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_TRNFL,GR_STSFL,gr_LUSBY,GR_LUPDT,"+L_strYRDGT+" from HR_GRMST where GR_SBSCD='"+M_strSBSCD+"' and GR_GRDCD='"+txtGRDCD.getText()+"'");
						*/
					}
					else if(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(cl_dat.M_strYEDDT_pbst))>0)
					{
						JOptionPane.showMessageDialog(this, "Modification for next finantial year is not allowed ..", "Error", JOptionPane.ERROR_MESSAGE) ;
						return;
					}
					else
					{
					    // COMMENTED TEMPORARILY ON 02/05/2005
						/*System.out.println(3);
						cl_dat.exeSQLUPD("Insert into HR_GRMAM (GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_TRNFL,GR_STSFL,gr_LUSBY,GR_LUPDT,GR_YRDGT) select GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_TRNFL,GR_STSFL,gr_LUSBY,GR_LUPDT,"+L_strYRDGT+" from HR_GRMST where GR_SBSCD='"+M_strSBSCD+"' and GR_GRDCD='"+txtGRDCD.getText()+"'","setLCLPD");
						System.out.println("3 : Insert into HR_GRMAM (GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_TRNFL,GR_STSFL,gr_LUSBY,GR_LUPDT,GR_YRDGT) select GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_TRNFL,GR_STSFL,gr_LUSBY,GR_LUPDT,"+L_strYRDGT+" from HR_GRMST where GR_SBSCD='"+M_strSBSCD+"' and GR_GRDCD='"+txtGRDCD.getText()+"'");*/
					}
					M_strSQLQRY="UPDATE "+(L_flgOLDDT==true ? "HR_GRMAM" : "HR_GRMST")+" SET "
						+"GR_SPALW = "+nvlSTRVL(txtSPALW.getText().trim(),"0")+","
						+"GR_LNSUB = "+nvlSTRVL(txtLNSUB.getText().trim(),"0")+","
						+"GR_CONVY = "+nvlSTRVL(txtCONVY.getText().trim(),"0")+","
						+"GR_CHEDN = "+nvlSTRVL(txtCHEDN.getText().trim(),"0")+","
						+"GR_MDALW = "+nvlSTRVL(txtMDALW.getText().trim(),"0")+","
						+"GR_MDAFL = "+(chkMDAFL.isSelected()? "'P'":"'A'")+","
						+"GR_LTALW = "+nvlSTRVL(txtLTALW.getText().trim(),"0")+","
						+"GR_LTAFL = "+(chkLTAFL.isSelected()? "'P'":"'A'")+","
						+"GR_PFALW = "+nvlSTRVL(txtPFALW.getText().trim(),"0")+","
						+"GR_SAALW = "+nvlSTRVL(txtSAALW.getText().trim(),"0")+","
						+"GR_HRALW = "+nvlSTRVL(txtHRALW.getText().trim(),"0")+","
						+"GR_DNALW = "+nvlSTRVL(txtDNALW.getText().trim(),"0")+","
						+"GR_WKALW = "+nvlSTRVL(txtWKALW.getText().trim(),"0")+","
						+"GR_WSALW = "+nvlSTRVL(txtWSALW.getText().trim(),"0")+","
						+"GR_GRALW = "+nvlSTRVL(txtGRALW.getText().trim(),"0")+","
						+"GR_CRALW = "+nvlSTRVL(txtCRALW.getText().trim(),"0")+","
						+getUSGDTL("GR",'U',"0")+" where GR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and GR_GRDCD='"+txtGRDCD.getText().trim()+"'"//TG_LUPDT
						+(L_flgOLDDT==true ? " and GR_YRDGT="+L_strYRDGT : "");//EP_LUPDT
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
                    setMSG("Modification Completed..",'N');
                    clrCOMP();
                }
                else
					setMSG("Modification Failed..",'E');
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			catch(Exception L_E)
			{
				cl_dat.M_flgLCUPD_pbst=false;
				cl_dat.exeDBCMT("exeSAVE");
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG(L_E,"exeADDREC");
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
	
	public void focusGained(FocusEvent P_FE)
	{
		super.focusGained(P_FE);
		if(!M_flgERROR)
		{
			if(M_objSOURC == txtGRDCD)
				setMSG("Please enter Grade Code, 'F1' for help ..",'N');
			else if(M_objSOURC == txtSPALW)
				setMSG("Please enter Special Allowance ..",'N');
			else if(M_objSOURC == txtLNSUB)
				setMSG("Please enter Lunch Subsidy ..",'N');
			else if(M_objSOURC == txtCONVY)
				setMSG("Please enter Conveyance ..",'N');
			else if(M_objSOURC == txtCHEDN)
				setMSG("Please enter Child Education Allowance ..",'N');
			else if(M_objSOURC == txtMDALW)
				setMSG("Please enter Medical Allowance ..",'N');
			   
		}
	}

	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			System.out.println("inside veeee2");
			try
			{
				System.out.println("inside veeee1");
				if(((JTextField)input).getText().length() == 0)
					return true;
				System.out.println("inside veeee");
				if(input == txtGRDCD)
				{
					M_strSQLQRY="select cmt_codcd from co_cdtrn where cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HRXXGRD' and cmt_codcd ='"+txtGRDCD.getText().trim().toUpperCase()+"'";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						M_strSQLQRY+=" and cmt_codcd not in (select GR_GRDCD from HR_GRMST where GR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"')";
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						M_strSQLQRY+=" and cmt_codcd in(select GR_GRDCD from HR_GRMST where GR_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"')";
					System.out.println("verify>>"+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null && M_rstRSSET.next())
					{
						txtGRDCD.setText(txtGRDCD.getText().trim().toUpperCase());
						return true;
					}
					else
					{
						setMSG("Enter Valid Grade Code",'E');
						return false;
					}
								
				}	
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"class INPVF");		
			}
			return true;
		}
	}

}