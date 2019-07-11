/*
System Name   : System Administration
Program Name  : User Defination
Author        : AAP

Modificaitons 
Modified By    : AAP
Modified Date  : 26/04/2004
Modified det.  : Revision for "Authorisatin" option, User type Modification and deletion. User addition facility removed 
Version        : v2.0.1
*/
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Vector;

/**
<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT> <TABLE border=1 borderColorDark=darkslateblue borderColorLight=white cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%">    <TR>    <TD>System Name</TD>    <TD>System Administration</TD></TR>  <TR>    <TD>Program Name</TD>    <TD>User Defination</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>Form for adding, modifying, and deleting User types</TD></TR>  <TR>    <TD>Basis Document</TD>    <TD></TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp2\sa_usrdf.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>f:\source\splerp2\sa_usrdf.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>&nbsp; </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Modification&nbsp;: 1 </STRONG></TD>    <TD></TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD>    <TD></TD></TR>  <TR>    <TD>      <P       align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P></TD>    <TD>      <P align=left>27/04/2004</P></TD></TR>  <TR>    <TD>      <P       align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD>    <TD>2.0.1</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE background="" border=1 borderColorDark=white borderColorLight=darkslateblue cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width="100%">    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>SA_PPRTR</TD>    <TD>PPR_SYSCD,PPR_USRTP,PPR_PRGCD,PPR_SBSCD</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>*/
class sa_usrdf extends cl_pbase 
{
	/**JList for Program Name*/
	private JList		lstPRGNM;/**JList for Sub System Code*/
	private JList		lstSBSDS;/**Vector for Sub System Details*/
	private Vector<String>		vtrSBSDS;/**Vector for Sub System Code*/
	private Vector<String>		vtrSBSCD;/**Vector for Program Name*/
	private Vector<String>		vtrPRGNM;/**Combo Box for System Code*/
	//private JCheckBox   chkNEWSBS; // CheckBox for new subsystem code
	private JComboBox	cmbSYSCD;/**Combo Box for User Type*/
	private JComboBox	cmbUSRTP;/**TextField for User Type*/
	private JTextField	txtUSRTP;/**Table for User Type Rights details */
	private cl_JTable	tblDATA;/** Button to add data to tblDATA*/
	private JButton btnADD;/** Internal Panels for dynamic component layout*/
	private JPanel pnlSBSDS,pnlPRGCD;	/** Count of old data rows in tblDATA in modification*/
	private int intOLDDAT;
	/**
	 * To construct the form
	 * 
	 * To construct the form
	 * 
	 * <p>Lays the components, populates the SYSCD combo from SA_SPMST
	 */
	sa_usrdf()
	{
		super(2);
		vtrSBSDS=new Vector<String>();
		vtrPRGNM=new Vector<String>();
		cmbSYSCD=new JComboBox();cmbUSRTP=new JComboBox();
		cmbSYSCD.addItem("Select");cmbUSRTP.addItem("Select");
		txtUSRTP=new JTextField();
		btnADD=new JButton("ADD");
		String[] L_staNAMES=new String[]{"FL","User Type","System Code","SBSCD","Program Code","ADD","MOD","DEL","ENQ","AUT"};
		int[] size=new int[]{20,75,75,75,75,50,50,50,50,50};
		tblDATA=crtTBLPNL1(new JPanel(null),L_staNAMES,10000,0,25,M_intCOLWD*4,150,size,new int[]{0,5,6,7,8,9}) ;
		tblDATA.setInputVerifier(new TBLINPVF());
		tblDATA.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		try
		{
			M_strSQLQRY="Select distinct SP_SYSNM,SP_SYSCD from SA_SPMST";		
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
					cmbSYSCD.addItem(M_rstRSSET.getString(2)+" "+M_rstRSSET.getString(1));
				M_rstRSSET.close();
			}
		}catch(Exception E)
		{			setMSG(E,"Child.Constructor");		}
		setMatrix(18,6);
		add(new JLabel("Select System"),1,1,1,1,this,'L');
		add(cmbSYSCD,1,2,1,2,this,'L');
		cmbSYSCD.setMaximumRowCount(15);
		add(new JLabel("Program Name"),1,5,1,1,this,'L');
		add(new JLabel("User Type"),2,1,1,1,this,'L');
		add(cmbUSRTP,2,2,1,1,this,'L');
		add(txtUSRTP,2,2,1,1,this,'L');
		//add(chkNEWSBS = new JCheckBox("New SBSCD"),2,3,1,1,this,'L');
		setMatrix(18,6);
		add(btnADD,12,1,1,1,this,'L');
		add(new JScrollPane(tblDATA,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),12,2,5,5,this,'L');
		setMatrix(18,4);
		setENBL(false);
	}
	/**
	 * <b>TASKS : ><br>
	 * Source = cmbOPTN : Show txtUSRTP in Addtion otherwise, Show cmbUSRTP. Set default fields enabled<br>
	 * Source = cmbSYSCD : Populate lstPRGCD and lstSBSDS. Populate cmbUSRTP if visible. <br>
	 * Source = cmbUSRTP : Show details in table<br>
	 * Source = btnADD : Insert all combinations of selected items in lstPRGDS and lstSBSDS into tblDATA. Put this data at end of existing in case of modificationc
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				cmbSYSCD.setEnabled(true);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				{
					txtUSRTP.setVisible(true);txtUSRTP.setEnabled(true);
					cmbUSRTP.setVisible(false);
				}
				else
				{
					txtUSRTP.setVisible(false);
					cmbUSRTP.setVisible(true);cmbUSRTP.setEnabled(true);
				}
				cmbSYSCD.requestFocus();
				cmbSYSCD.showPopup();
			}
			if(M_objSOURC==cmbSYSCD)
			{
				vtrSBSDS=new Vector<String>();vtrSBSCD=new Vector<String>();
				vtrPRGNM=new Vector<String>();
//GETTING DIFFERENT USER TYPES FOR SELECTED SYS
				if(cmbUSRTP.isVisible())
				{
					cmbUSRTP.removeAllItems();
					cmbUSRTP.addItem("Select");
					M_strSQLQRY="select distinct PPR_USRTP from SA_PPRTR where PPR_SYSCD='"+cmbSYSCD.getSelectedItem().toString().substring(0,2)+"' order by PPR_USRTP";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!=null)
						while(M_rstRSSET.next())
							cmbUSRTP.addItem(M_rstRSSET.getString(1));
					cmbUSRTP.requestFocus();
					cmbUSRTP.showPopup();
				}
				else
					txtUSRTP.requestFocus();
//GETTING LIST OF FORMS AVAILABLE IN THE SELECTED SYSTEM
				//M_strSQLQRY="select distinct PP_PRGCD from SA_PPMST where PP_SYSCD='"+cmbSYSCD.getSelectedItem().toString().substring(0,2)+"' and isnull(PP_CNVFL,' ') "+(chkNEWSBS.isSelected() ? "='Y'" : "<>'Y'")+" order by PP_PRGCD";
				M_strSQLQRY="select distinct PP_PRGCD from SA_PPMST where PP_SYSCD='"+cmbSYSCD.getSelectedItem().toString().substring(0,2)+"' order by PP_PRGCD";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
						vtrPRGNM.addElement(M_rstRSSET.getString(1));
					lstPRGNM=new JList(vtrPRGNM);
					lstPRGNM.setSelectionMode(2);
					setMatrix(18,6);
					if(pnlPRGCD!= null)
						pnlPRGCD.removeAll();
					else
						pnlPRGCD=new JPanel (null);
					add(new JScrollPane(lstPRGNM),1,1,9,1,pnlPRGCD,'L');
					add(pnlPRGCD,1,6,9.9,1.1,this,'L');
					setMatrix(18,4);
					updateUI();
				}
//GETTING LIST OF SUBSYSTEM CODES AVAILABLE IN THE SELECTED SYSTEM				
				//M_strSQLQRY="select * from co_cdtrn where CMT_CODCD like '"+cmbSYSCD.getSelectedItem().toString().substring(0,2)+"%' and CMT_CGMTP = '"+(chkNEWSBS.isSelected() ? "MST" : "SYS")+"' and CMT_CGSTP='COXXSBS' order by CMT_CODCD";
				M_strSQLQRY="select * from co_cdtrn where CMT_CODCD like '"+cmbSYSCD.getSelectedItem().toString().substring(0,2)+"%' and CMT_CGMTP = 'MST' and CMT_CGSTP='COXXSBS' order by CMT_CODCD";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					vtrSBSCD=new Vector<String>();
					vtrSBSDS=new Vector<String>();
					while(M_rstRSSET.next())
					{
						vtrSBSDS.add(M_rstRSSET.getString("CMT_CODDS")+"    "+M_rstRSSET.getString("CMT_CODCD"));
						vtrSBSCD.add(M_rstRSSET.getString("CMT_CODCD"));
					}
				}
				M_rstRSSET.close();
				lstSBSDS=new JList(vtrSBSDS);
				if(pnlSBSDS!=null)
					pnlSBSDS.removeAll();
				else
					pnlSBSDS=new JPanel (null);
				add(new JScrollPane(lstSBSDS),1,1,7,3.3,pnlSBSDS,'L');
				add(pnlSBSDS,3,1,8,3.4,this,'L');
				updateUI();
			}
			else if(M_objSOURC==cmbUSRTP)
			{
				intOLDDAT=0;
				M_strSQLQRY="SELECT * from SA_PPRTR where PPR_USRTP='"+cmbUSRTP.getSelectedItem().toString()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY);
				int L_intROWID=-1;
				if(M_rstRSSET!=null)
				{
					tblDATA.clrTABLE();
					while(M_rstRSSET.next())
					{
						L_intROWID++;
						tblDATA.setValueAt(M_rstRSSET.getString("PPR_USRTP"),L_intROWID,1);
						tblDATA.setValueAt(M_rstRSSET.getString("PPR_SYSCD"),L_intROWID,2);
						tblDATA.setValueAt(M_rstRSSET.getString("PPR_SBSCD"),L_intROWID,3);
						tblDATA.setValueAt(M_rstRSSET.getString("PPR_PRGCD"),L_intROWID,4);
						tblDATA.setValueAt((nvlSTRVL(M_rstRSSET.getString("PPR_ADDFL"),"").equalsIgnoreCase("Y")==true ? Boolean.TRUE : Boolean.FALSE),L_intROWID,5);
						tblDATA.setValueAt((nvlSTRVL(M_rstRSSET.getString("PPR_MODFL"),"").equalsIgnoreCase("Y")==true ? Boolean.TRUE : Boolean.FALSE),L_intROWID,6);
						tblDATA.setValueAt((nvlSTRVL(M_rstRSSET.getString("PPR_DELFL"),"").equalsIgnoreCase("Y")==true ? Boolean.TRUE : Boolean.FALSE),L_intROWID,7);
						tblDATA.setValueAt((nvlSTRVL(M_rstRSSET.getString("PPR_ENQFL"),"").equalsIgnoreCase("Y")==true ? Boolean.TRUE : Boolean.FALSE),L_intROWID,8);
						tblDATA.setValueAt((nvlSTRVL(M_rstRSSET.getString("PPR_AUTFL"),"").equalsIgnoreCase("Y")==true ? Boolean.TRUE : Boolean.FALSE),L_intROWID,9);
					}
						intOLDDAT=L_intROWID;
				}
				M_rstRSSET.close();
				if(L_intROWID==-1)
					setMSG("No rights added for the user type ..",'E');
			}
			if(M_objSOURC==btnADD)
			{
				int row=0;
				if(txtUSRTP.isVisible())
					tblDATA.clrTABLE();
				else
					for(row=0;row<tblDATA.getRowCount();row++)
						if(tblDATA.getValueAt(row,1).toString().length()==0)
							break;
				int[] L_PRGNM=lstPRGNM.getSelectedIndices();
				if(L_PRGNM.length!=0)
				{
					int [] L_SBSDS=lstSBSDS.getSelectedIndices();
					for(int i=0;i<L_PRGNM.length;i++)
					{
						for(int j=0;j<L_SBSDS.length;j++)
						{
							tblDATA.setValueAt((txtUSRTP.isVisible()==true ? txtUSRTP.getText() : cmbUSRTP.getSelectedItem()),row,1);
							tblDATA.setValueAt(new Boolean(true),row,0);
							tblDATA.setValueAt(cmbSYSCD.getSelectedItem().toString().substring(0,2),row,2);
							tblDATA.setValueAt(vtrSBSCD.elementAt(L_SBSDS[j]).toString(),row,3);
							tblDATA.setValueAt(vtrPRGNM.elementAt(L_PRGNM[i]).toString(),row,4);
							row++;
						}
					}
				}
			}
		}catch (Exception e)
		{			setMSG(e,"Child.actionPerformed");		}
	}
	
	boolean vldDATA()
	{
		return(true);
	}
	/**
	 * To save data
	 * 
	 * To save data
	 * 
	 * <p>Addition : Put data directly into SA_PPRTR<br>
	 * Modification : If record is from olddata, modify SA_PPRTR, otherwise insert into SA_PPRTR. intOLDDAT is used for this identification<br>
	 * Deletion : If user of this type exists, ehow error msg. and return; otherwise, delete corresponding rows from SA_PPRTR
	 */
	void exeSAVE()
	{
		try
		{
			if(vldDATA())
			{
				cl_dat.M_flgLCUPD_pbst=true;
				Boolean TRUE=new Boolean(true);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{//new USER TYPE IS BEING ADDED
					for(int i=0;i<tblDATA.getRowCount()&&tblDATA.getValueAt(i,0).equals((Object)TRUE)&&cl_dat.M_flgLCUPD_pbst;i++)
					{//ADDING DATA FROM JTABLE TO SA_PPRTR
						M_strSQLQRY="INSERT INTO SA_PPRTR (PPR_SYSCD,PPR_SBSCD,PPR_PRGCD,PPR_USRTP,PPR_ADDFL,PPR_MODFL,PPR_DELFL,PPR_ENQFL,PPR_AUTFL,PPR_TRNFL,PPR_STSFL,PPR_LUSBY,PPR_LUPDT) VALUES ( "
								+"'"+tblDATA.getValueAt(i,2).toString()+"',"//PPR_SYSCD
								+"'"+tblDATA.getValueAt(i,3).toString()+"',"//PPR_SBSCD
								+"'"+tblDATA.getValueAt(i,4).toString()+"',"//PPR_PRGCD
								+"'"+tblDATA.getValueAt(i,1).toString()+"',"//PPR_USRTP
								+"'"+(tblDATA.getValueAt(i,5).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_ADDFL
								+"'"+(tblDATA.getValueAt(i,6).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_MODFL
								+"'"+(tblDATA.getValueAt(i,7).equals((Object) TRUE) ? "Y" : "N")+"',"//PR_DELFL
								+"'"+(tblDATA.getValueAt(i,8).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_ENQFL
								+"'"+(tblDATA.getValueAt(i,9).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_AUTFL
								+"'"+"0"+"',"//PPR_TRNFL    
								+"'"+""+"',"//PPR_STSFL    
								+"'"+cl_dat.M_strUSRCD_pbst+"',"//PPR_LUSBY    
								+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";//PPR_LUPDT    
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{//USER TYPE IS BEIBG MODIFIED
					for(int i=0;i<tblDATA.getRowCount()&&tblDATA.getValueAt(i,1).toString().length()>0&&cl_dat.M_flgLCUPD_pbst;i++)
					{//ADDING DATA FROM JTABLE TO SA_PPRTR
						if(i>intOLDDAT)
						{
							M_strSQLQRY="INSERT INTO SA_PPRTR (PPR_SYSCD,PPR_SBSCD,PPR_PRGCD,PPR_USRTP,PPR_ADDFL,PPR_MODFL,PPR_DELFL,PPR_ENQFL,PPR_AUTFL,PPR_TRNFL,PPR_STSFL,PPR_LUSBY,PPR_LUPDT) VALUES ( "
									+"'"+tblDATA.getValueAt(i,2).toString()+"',"//PPR_SYSCD
									+"'"+tblDATA.getValueAt(i,3).toString()+"',"//PPR_SBSCD
									+"'"+tblDATA.getValueAt(i,4).toString()+"',"//PPR_PRGCD
									+"'"+tblDATA.getValueAt(i,1).toString()+"',"//PPR_USRTP
									+"'"+(tblDATA.getValueAt(i,5).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_ADDFL
									+"'"+(tblDATA.getValueAt(i,6).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_MODFL
									+"'"+(tblDATA.getValueAt(i,7).equals((Object) TRUE) ? "Y" : "N")+"',"//PR_DELFL
									+"'"+(tblDATA.getValueAt(i,8).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_ENQFL
									+"'"+(tblDATA.getValueAt(i,9).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_AUTFL
									+"'"+"0"+"',"//PPR_TRNFL    
									+"'"+""+"',"//PPR_STSFL    
									+"'"+cl_dat.M_strUSRCD_pbst+"',"//PPR_LUSBY    
									+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";//PPR_LUPDT
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						}
						else if(tblDATA.getValueAt(i,0).equals(Boolean.TRUE))
						{
							M_strSQLQRY="UPDATE SA_PPRTR SET  "
									+"PPR_ADDFL='"+(tblDATA.getValueAt(i,5).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_ADDFL
									+"PPR_MODFL='"+(tblDATA.getValueAt(i,6).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_MODFL
									+"PPR_DELFL='"+(tblDATA.getValueAt(i,7).equals((Object) TRUE) ? "Y" : "N")+"',"//PR_DELFL
									+"PPR_ENQFL='"+(tblDATA.getValueAt(i,8).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_ENQFL
									+"PPR_AUTFL='"+(tblDATA.getValueAt(i,9).equals((Object) TRUE) ? "Y" : "N")+"',"//PPR_AUTFL
									+"PPR_TRNFL='"+"0"+"',"//PPR_TRNFL    
									+"PPR_STSFL='"+""+"',"//PPR_STSFL    
									+"PPR_LUSBY='"+cl_dat.M_strUSRCD_pbst+"',"//PPR_LUSBY    
									+"PPR_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' where "//PPR_LUPDT    
									+"PPR_SYSCD='"+tblDATA.getValueAt(i,2).toString()+"' and "//PPR_SYSCD
									+"PPR_SBSCD='"+tblDATA.getValueAt(i,3).toString()+"' and "//PPR_SBSCD
									+"PPR_PRGCD='"+tblDATA.getValueAt(i,4).toString()+"' and "//PPR_PRGCD
									+"PPR_USRTP='"+tblDATA.getValueAt(i,1).toString()+"'";//PPR_USRTP
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						}
					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					M_rstRSSET=cl_dat.exeSQLQRY0("Select count(*) COUNT from sa_ustrn where ust_usrtp='"+cmbUSRTP.getSelectedItem()+"'");
					if(M_rstRSSET!=null)
						if(M_rstRSSET.next())
						{
							if(Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("COUNT"),"0"))>0)
							{
								JOptionPane.showMessageDialog(this,M_rstRSSET.getString("COUNT") +" Users exist in this type.. \n User type cannot be deleted ..","Error ..",JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
					M_rstRSSET.close();
					M_strSQLQRY="Delete from sa_pprtr where ppr_usrtp='"+cmbUSRTP.getSelectedItem()+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				if(cl_dat.M_flgLCUPD_pbst)
				{
					cl_dat.exeDBCMT("exeSAVE");
					setMSG("User Added ..",'N');
				}
				else
					setMSG("Error occured during saving ..",'E');
			}
		}catch (Exception e)
		{setMSG(e,"Error in exeSAVE : ");
		 setMSG("Error occured during saving ..",'E');}
	}
	private class TBLINPVF extends TableInputVerifier
	{
		/**Set First column checked if value is changed		 */
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(P_intCOLID>4)
					tblDATA.setValueAt(Boolean.TRUE, P_intROWID,0);
				
			}catch(Exception e)
			{
				setMSG(e,"Child.TBLINPVF");
				return false;
			}
			return true;
		}
	}
}