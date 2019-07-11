import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Process&nbsp;Resource&nbsp;Planning System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>Location Master</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                    Form for       adding&nbsp;and retrieving&nbsp; details of Internal Stores Locations </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\dada\asoft\exec\splerp2\pr_melcm.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\pr_melcm.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>15/05/2003 </TD></TR>  <TR>    <TD>Version </TD>    <TD>1.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>For Base classes revision and Subsystem implementation</TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD>AAP</TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>25/09/2003</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD>2.0.0.</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>PR_LCMST</TD>    <TD> LC_LOCID</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>*/
class pr_melcm extends cl_pbase
{
	/**Location ID	 */
	TxtLimit	txtLOCID;/**Location Description */
	TxtLimit	txtLOCDS;/**Location Capacity */
	TxtNumLimit	txtLCCAP;/**Location Stock */
	TxtNumLimit txtLCSTK;/**UOM of capacity */
	JComboBox	cmbCPUOM;
/** Constructor to create the screen. No Sql queries involved */
	pr_melcm()
	{
		super(2);
		txtLOCID=new TxtLimit(5);txtLOCDS=new TxtLimit(20);txtLCCAP=new TxtNumLimit(7.3);txtLCSTK=new TxtNumLimit(7.3);
		cmbCPUOM=new JComboBox();cmbCPUOM.addItem("Select");cmbCPUOM.addItem("MT");cmbCPUOM.addItem("Kg");cmbCPUOM.addItem("gm");
		cmbCPUOM.addItem("PC");
		
		setMatrix(18,4);
		add(new JLabel("Location ID : "),1,2,1,1,this,'L');
		add(txtLOCID,1,3,1,1,this,'L');
		add(new JLabel("Location Description : "),2,2,1,1,this,'L');
		add(txtLOCDS,2,3,1,1,this,'L');
		add(new JLabel("Location Capacity : "),3,2,1,1,this,'L');
		add(txtLCCAP,3,3,1,1,this,'L');
		add(new JLabel("Capacity UOM : "),4,2,1,1,this,'L');
		add(cmbCPUOM,4,3,1,1,this,'L');
		add(new JLabel("Location Stock : "),5,2,1,1,this,'L');
		add(txtLCSTK,5,3,1,1,this,'L');
	}
	/** Validates data entered by user	 */
	boolean vldDATA()
	{
		if(txtLOCID.getText().length()==0)
		{
			txtLOCID.requestFocus();
			setMSG("Pl. enter Location ID .. ",'E');
			return false;
		}
		if(txtLOCDS.getText().length()==0)
		{
			txtLOCDS.requestFocus();
			setMSG("Pl. enter Location Description .. ",'E');
			return false;
		}
		if(txtLCCAP.getText().length()==0)
		{
			txtLCCAP.requestFocus();
			setMSG("Pl. enter Location Capacity .. ",'E');
			return false;
		}
		if(cmbCPUOM.getSelectedIndex()==0)
		{
			cmbCPUOM.requestFocus();
			setMSG("Pl. select UOM for capacity .. ",'E');
			return false;
		}
		txtLOCDS.setText(txtLOCDS.getText().toUpperCase());
		txtLOCID.setText(txtLOCID.getText().toUpperCase());
		txtLCSTK.setText("0");
		return true;
	}
	/** To save/modify data	 */
	void exeSAVE()
	{
		this.setCursor(cl_dat.M_curDFSTS_pbst);
		if(vldDATA())
		{
			if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPADD_pbst))
			{
				M_strSQLQRY="INSERT INTO pr_LCMST (LC_LOCDS,LC_LOCID,LC_LCCAP,LC_LCSTK,LC_CPUOM,LC_TRNFL,LC_STSFL,LC_LUSBY,LC_LUPDT) VALUES ("
					+"'"+txtLOCDS.getText()+"',"//LC_LOCDS
					+"'"+txtLOCID.getText()+"',"//LC_LOCID
					+""+txtLCCAP.getText()+","//LC_LCCAP
					+""+txtLCSTK.getText()+","//LC_LCSTK
					+"'"+cmbCPUOM.getSelectedItem().toString()+"',"//LC_CPUOM
					+getUSGDTL("LC",'i',"")+")";//LC_TRNFL,LC_STSFL,LC_LUSBY,LC_LUPDT
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					
					setMSG("Location Created ..",'N');
					clrCOMP();
				}
				else
				{
					setMSG("Addition falied..",'E');
				}
			}
			else if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY="UPDATE pr_LCMST SET "
					+"LC_LOCDS='"+txtLOCDS.getText()+"',"
					+"LC_LCCAP= "+txtLCCAP.getText()+","
					+"LC_LCSTK= "+txtLCSTK.getText()+","
					+"LC_CPUOM='"+cmbCPUOM.getSelectedItem().toString()+"',"
					+getUSGDTL("LC",'u',null)+")";//LC_TRNFL,LC_STSFL,LC_LUSBY,LC_LUPDT
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setMSG("Location Created ..",'N');
					clrCOMP();
				}
				else
				{
					setMSG("Modification falied..",'E');
				}
			}
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/** To retrieve data in enquirey/modification option	 */
	void getDATA()
	{
		this.setCursor(cl_dat.M_curWTSTS_pbst);
		M_strSQLQRY="SELECT * FROM pr_LCMST WHERE LC_LOCID='"+txtLOCID.getText().trim().toUpperCase()+"'";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
		try
		{
			while(M_rstRSSET.next())
			{
				txtLOCID.setText(M_rstRSSET.getString("LC_LOCID"));
				txtLOCDS.setText(M_rstRSSET.getString("LC_LOCDS"));
				txtLCSTK.setText(M_rstRSSET.getString("LC_LCSTK"));
				txtLCCAP.setText(M_rstRSSET.getString("LC_LCCAP"));
				cmbCPUOM.setSelectedItem(M_rstRSSET.getString("LC_CPUOM"));
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}catch(Exception e)
		{
			setMSG("Location does not Exist ..",'E');
			System.out.println(e);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**<b> TASKS</b><br>
	 * &nbsp&nbsp&nbsp&nbsptxtLOCID : getDATA()
	 * <br>&nbsp&nbsp&nbsp&nbspNavigation control & focus selection on cmbOPTN	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
		{
			String L_STRTMP=cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString();
			if((L_STRTMP).equals(cl_dat.M_OPADD_pbst))
			{
				txtLOCID.requestFocus();
			}
			
			else if((L_STRTMP).equals(cl_dat.M_OPDEL_pbst))
			{
				txtLOCID.setEnabled(true);
				txtLOCID.requestFocus();
			}
			else if((L_STRTMP).equals(cl_dat.M_OPMOD_pbst))
			{
				txtLOCID.requestFocus();
			}
			else if((L_STRTMP).equals(cl_dat.M_OPENQ_pbst))
			{
				txtLOCID.setEnabled(true);
				txtLOCID.requestFocus();
			}
		}
		else if(M_objSOURC==txtLOCID)
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				getDATA();
			}
		}
		//NAVIGATION CONTROL
		else if(M_objSOURC==txtLOCID)
			txtLOCDS.requestFocus();
		else if(M_objSOURC==txtLOCDS)
			txtLCCAP.requestFocus();
		else if(M_objSOURC==txtLCCAP)
			cmbCPUOM.requestFocus();
		else if(M_objSOURC==cmbCPUOM)
			txtLCSTK.requestFocus();
	}
	/**<b> TASKS</b>
	 * <br>&nbsp&nbsp&nbsp&nbsp txtLOCID : HELP to display available locaitons
	 * <br>&nbsp&nbsp&nbsp&nbsp cmbCPUOM : Focus transfer
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int key=L_KE.getKeyCode();
		if(key==L_KE.VK_F1&&M_objSOURC==txtLOCID)
		{
			M_strSQLQRY="SELECT LC_LOCID,LC_LOCDS FROM pr_LCMST ORDER BY LC_LOCID";
			M_strHLPFLD = "txtLOCID";
			cl_hlp(M_strSQLQRY ,1,1,new String[] {"Location ID","Description"},2,"CT");
		}
		else if(key==L_KE.VK_ENTER&&M_objSOURC==cmbCPUOM)
		{
			L_KE.setKeyCode(L_KE.VK_TAB);
			cmbCPUOM.transferFocus();
		}
	}
	public void keyReleased(KeyEvent L_KE)
	{
	}
	
	public void exeHLPOK()
	{
		try
		{
			if(M_strHLPFLD.equals("txtLOCID"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				txtLOCID.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}catch(Exception L_EX)
		{
			System.out.println("Exception in exeHLPOK : "+L_EX);
			setMSG("Error in child exeHLPOK()..",'E');
		}
	}
}
