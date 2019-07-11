/*
System Name   : System Administration
Program Name  : User Profile
Program Desc. : To add/mod/del user and his/her rights profile
				
Author        : AAP
Date          : 
Version       : 

Modificaitons 
Modified By    : AAP
Modified Date  : 26/04/2004
Modified det.  : Employee code addition, Old Bugs removal
Version        : v2.0.1
*/

import javax.swing.*;
import java.awt.event.*;
import java.util.StringTokenizer;import java.util.Hashtable;import java.util.Vector;import java.awt.Dimension;

/**<BODY><P>Program Description :</P>			<P><FONT color=purple><STRONG>Program Details :</STRONG></FONT> 			<TABLE border=1 borderColorDark=darkslateblue borderColorLight=white cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%">			  <TR><TD>System Name</TD><TD>System Administration</TD></TR><TR><TD>Program Name</TD><TD>User Profile</TD></TR><TR><TD>Program Desc</TD><TD>To create,modify,delete user profile &amp;   user</TD></TR><TR><TD>Basis Document</TD><TD>&nbsp;</TD></TR><TR><TD>Executable path</TD><TD>f:\exec\splerp2\sa_usrpf.class</TD></TR><TR><TD>Source path</TD><TD>f:\source\splerp2\sa_usrpf.java</TD></TR><TR><TD>Author </TD><TD>AAP </TD></TR><TR><TD>Date</TD><TD>&nbsp; </TD></TR><TR><TD>Version </TD><TD>2.0.0</TD></TR><TR><TD><STRONG>Modification&nbsp;: 1 </STRONG></TD><TD></TD></TR><TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD>AAP</TD></TR><TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Date</P></TD><TD><P align=left>27/04/2004</P></TD></TR><TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD>2.0.1</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE background="" border=1 borderColorDark=white borderColorLight=darkslateblue cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width="100%">			  <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD><TD><P align=center>Add</P></TD><TD><P align=center>Mod</P></TD><TD><P align=center>Del</P></TD><TD><P align=center>Enq</P></TD></TR><TR><TD>SA_PPRTR</TD><TD>PPR_SYSCD,PPR_PRGCD,PPR_USRTP</TD><TD></TD><TD></TD><TD></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR><TR><TD>SA_USMST</TD><TD>US_USRCD</TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>SA_USTRN</TD>    <TD>UST_USRCD,UST_USRTP,UST_SYSCD</TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR><TR><TD>HR_EPMST</TD><TD>EP_EMPNO (To retrieve details of Name,Email)</TD><TD><P align=center>&nbsp;</P></TD><TD><P align=center>&nbsp;</P></TD><TD><P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P></BODY>			 */

public class sa_usrpf extends cl_pbase
{
	/**TextField used as cellEditor in table of User Rights for User Code*/
	private TxtLimit txtUSRCD;/**TextField used as cellEditor in table of User Rights for User Name*/
	private TxtLimit txtUSRNM;/**TextField used as cellEditor in table of User Rights for Email*/
	private TxtLimit txtEMAIL;/**Table to display User and User Rights*/
	private cl_JTable	tblUSRRT;/**Table to display Right Details*/
	private cl_JTBL tblRTDTL;/**Vector to keep System codes*/
	private Vector<String>	vtrSYSCD;/**Vector for User Types*/
	private Vector<String> vtrUSRTP;/**Vector for storing information at the time of Addition or Modification of User*/
	private Vector<String> vtrADMOD;/**vector to store User Codes*/
	private Vector<String> vtrUSRCD;/**Contains user type for user under each System*/
	private Hashtable<String,String[]> hstUSRTP;/**Array of String to keep the column names of table*/
	private String[] staNAMES;/**Array of User Codes for comparison at the time of keypressed F1*/
	private String[] strUSRCD;/**Array of ComoBoxes to keep User Types of a user under each system*/
	private JComboBox[] cmbSYSCD;/**Button for displaying data*/
	private JButton btnRTDTL;/**Panel for show details button*/
	private JPanel	pnlSCRIN;

	/**Constructs the form and retrieves details of SYSCD from backend	 */
	public sa_usrpf()
	{
		super(2);
		try
		{
			vtrADMOD=new Vector<String>();vtrSYSCD=new Vector<String>();vtrUSRCD=new Vector<String>();
			pnlSCRIN=new JPanel(null);
			M_strSQLQRY="SELECT DISTINCT SP_SYSCD FROM SA_SPMST";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
				while(M_rstRSSET.next())
					vtrSYSCD.addElement(M_rstRSSET.getString(1));
			M_strSQLQRY="SELECT COUNT (*) FROM SA_USMST";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			M_rstRSSET.next();
			strUSRCD=new String[Integer.parseInt(M_rstRSSET.getString(1))];
			M_strSQLQRY="SELECT DISTINCT US_USRCD FROM SA_USMST";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			int z=0;
			if(M_rstRSSET!=null)
				while(M_rstRSSET.next())
					strUSRCD[z++]=M_rstRSSET.getString("US_USRCD");
		}catch (Exception E)
		{			setMSG(E,"Child.constructor");		}
	}

	/**CALLED AT THE TIME OF SELECTION WHETHER ADDITION/MODIFICATION/DELETION.
	CREATES TABLES AND SELECTS DATA FROM SA_USMST AND SA_USTRN TO DISPLAY 
	IN TABLE FOR USER RIGHT*/
	private void getDATA()
	{
		try
		{
			staNAMES=new String[vtrSYSCD.size()+5];
			staNAMES[0]="FL";
			staNAMES[2]="User Code";
			staNAMES[3]="User Name";
			staNAMES[4]="E-mail";
			staNAMES[1]="Emp.Cd.";
			int[] L_inaCOLSZ=new int[staNAMES.length];
			L_inaCOLSZ[0]=20;
			L_inaCOLSZ[2]=50;
			L_inaCOLSZ[3]=100;
			L_inaCOLSZ[4]=100;
			L_inaCOLSZ[1]=50;
			for(int i=0;i<vtrSYSCD.size();i++)
			{
				staNAMES[i+5]=vtrSYSCD.elementAt(i).toString();
				L_inaCOLSZ[i+5]=40;
			}
			M_strSQLQRY="SELECT COUNT (*) FROM SA_USMST";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			M_rstRSSET.next();
			setMatrix(18,4);/**Sets the screen into 18 rows and 4 columns.*/
			/**Creating JTable.*/
			pnlSCRIN.removeAll();
			tblUSRRT=crtTBLPNL1(pnlSCRIN,staNAMES,Integer.parseInt(M_rstRSSET.getString(1))+1,1,1,7,4,L_inaCOLSZ,new int[]{0});
			tblUSRRT.setInputVerifier(new TBLINPVF());
			tblUSRRT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblUSRRT.setCellEditor(2,txtUSRCD=new TxtLimit(3));
			tblUSRRT.setCellEditor(3,txtUSRNM=new TxtLimit(25));
			tblUSRRT.setCellEditor(4,txtEMAIL=new TxtLimit(30));
			tblUSRRT.setCellEditor(1,new TxtNumLimit(5));
			tblUSRRT.cmpEDITR[1].addKeyListener(this);
			txtUSRCD.addFocusListener(this);txtUSRCD.addActionListener(this);txtUSRCD.addKeyListener(this);
			txtUSRNM.addFocusListener(this);txtUSRNM.addActionListener(this);txtUSRNM.addKeyListener(this);
			txtEMAIL.addFocusListener(this);txtEMAIL.addActionListener(this);txtEMAIL.addKeyListener(this);
			cmbSYSCD=new JComboBox[vtrSYSCD.size()];
			for (int i=0;i<cmbSYSCD.length;i++)
			{
				cmbSYSCD[i]=new JComboBox();
				cmbSYSCD[i].addFocusListener(this);
				cmbSYSCD[i].addKeyListener(this);
				cmbSYSCD[i].addItem("Select");
				tblUSRRT.setCellEditor(i+5,cmbSYSCD[i]);
			}
			hstUSRTP=new Hashtable<String,String[]>(10,0.2f);
			M_strSQLQRY="SELECT UST_USRCD,US_USRNM,US_EMLRF,US_EMPCD,UST_SYSCD,UST_USRTP FROM SA_USTRN,SA_USMST WHERE US_USRCD=UST_USRCD order by us_usrcd";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				String L_strSTRTMP="";
				int row=0;
				while(M_rstRSSET.next())
				{
					L_strSTRTMP=M_rstRSSET.getString("UST_SYSCD");
					for(int i=0;i<staNAMES.length;i++)
					{
						if(staNAMES[i].equals(L_strSTRTMP))
						{
							for(row=0;row<tblUSRRT.getRowCount()&&tblUSRRT.getValueAt(row,2).toString().length()>0;row++)
								if(tblUSRRT.getValueAt(row,2).toString().equals(M_rstRSSET.getString("UST_USRCD")))
									break;
							tblUSRRT.setValueAt(nvlSTRVL(M_rstRSSET.getString("UST_USRTP"),""),row,i);
							tblUSRRT.setValueAt(nvlSTRVL(M_rstRSSET.getString("UST_USRCD"),""),row,2);
							vtrUSRCD.addElement(nvlSTRVL(M_rstRSSET.getString("UST_USRCD"),""));
							tblUSRRT.setValueAt(nvlSTRVL(M_rstRSSET.getString("US_USRNM"),""),row,3);
							tblUSRRT.setValueAt(nvlSTRVL(M_rstRSSET.getString("US_EMLRF"),""),row,4);
							tblUSRRT.setValueAt(nvlSTRVL(M_rstRSSET.getString("US_EMPCD"),""),row,1);
						}
					}
				}
			}
			btnRTDTL=new JButton("Show Details");
			add(btnRTDTL,9,1,1.25,1,pnlSCRIN,'L');
			updateUI();
			
			staNAMES=new String[]{"FL","User Type","System Code","SBSCD","Program Code","ADD","MOD","DEL","ENQ","AUT"};
			L_inaCOLSZ=new int[]{20,75,75,75,75,70,70,70,70,70};
			tblRTDTL=(cl_JTBL)crtTBLPNL(pnlSCRIN,staNAMES,70,11,1,5,4,L_inaCOLSZ,new int[]{0,5,6,7,8,9}) ;
			tblRTDTL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			add(pnlSCRIN,1,1,18,4,this,'L');
			revalidate();
			updateUI();
		}catch(Exception e)
		{			setMSG(e," getDATA ");		}
	}
	
	/**THIS METHOD POPULATES THE COMBOBOX ON THE FOCUSGAIN BY SELECTING DATA FROM HASHTABLE.
	 * <p>Tasks:</p>
	 * <p> IF Source==cmbSYSCD[i] </p>
	 * <p> If Source is any of the Combo Box of any System Code then it checks for key in Hash Table</p>
	 * <p> key in hash table in concatination of system code and user code</p>
	 * <p> If Hash Table contains the key </p>
	 * <p> then it empties the combo box of that particul;ar system code and again add data into the combo box from the hash table</p>
	 * <p> Else if hash table doesnt have that key then</p>
	 * <p> It selects data from SA_USTRN where system code matches with the system code in vector containg system code at ith position and user code matches with user code mentioned in the table</p>
	 * <p> and add into the combo box and hash table simultaneously</p>*/
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try{
		for(int i=0;i<cmbSYSCD.length;i++)
		{
			if(M_objSOURC==cmbSYSCD[i])
			{
				if(hstUSRTP.containsKey(vtrSYSCD.elementAt(i).toString()+"|"+tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2).toString().toUpperCase()))
				{
					String[] L_strSTRTMP=(String[])hstUSRTP.get(vtrSYSCD.elementAt(i).toString()+"|"+tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2).toString().toUpperCase());
					cmbSYSCD[i].removeAllItems();
					cmbSYSCD[i].addItem("Select");

					for(int j=0;j<L_strSTRTMP.length;j++)
						cmbSYSCD[i].addItem(L_strSTRTMP[j]);
					
					cmbSYSCD[i].update(cmbSYSCD[i].getGraphics());cmbSYSCD[i].showPopup();
				}
				
				else
				{
					M_strSQLQRY="SELECT UST_USRTP FROM SA_USTRN WHERE UST_SYSCD='"+vtrSYSCD.elementAt(i).toString()+"' AND UST_USRCD='"+tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2).toString()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null)
					{
						cmbSYSCD[i].removeAllItems();
						cmbSYSCD[i].addItem("Select");
						
						while(M_rstRSSET.next())
							cmbSYSCD[i].addItem(M_rstRSSET.getString("UST_USRTP"));
						
						String[] L_strSTRTMP=new String[cmbSYSCD[i].getItemCount()-1];
						
						for(int j=1;j<cmbSYSCD[i].getItemCount();j++)
							L_strSTRTMP[j-1]=cmbSYSCD[i].getItemAt(j).toString();
						
						hstUSRTP.put(vtrSYSCD.elementAt(i).toString()+"|"+tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2).toString(),L_strSTRTMP);
					}
				}
			}
		}
		}catch(Exception e)
		{setMSG(e,"child FocusGained ");}
	}
	/**THIS METHOD CHECKS WHETHER NEW USER CODE IS PRESENT IN DATABASE
	 ON THE FOCUS LOST EVENT OS USER CODE COLUMN*/
	public void focusLost(FocusEvent L_FE)
	{
		try
		{
			super.focusLost(L_FE);
			if(M_objSOURC==txtUSRCD&&tblUSRRT.getSelectedRow()==tblUSRRT.getRowCount()-1)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					String L_strTEMP=tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2).toString().trim();
						for(int z=0;z<tblUSRRT.getRowCount()-1;z++)
						{
							if(L_strTEMP.equalsIgnoreCase(tblUSRRT.getValueAt(z,2).toString().trim()))
							{
								setMSG("User Already Exist Select New User",'E');
								JOptionPane.showMessageDialog(this,"User "+L_strTEMP.toUpperCase()+" already exists ..\nUse another user-code","Duplicate User",JOptionPane.ERROR_MESSAGE);
								tblUSRRT.setValueAt("",tblUSRRT.getSelectedRow(),1);
								tblUSRRT.editCellAt(tblUSRRT.getSelectedRow(),1);
								txtUSRCD.requestFocus();
						}
					}
				}
			}
			if(M_objSOURC == tblUSRRT.cmpEDITR[2] && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
			{
				if(tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2).toString().length()>0 || ((JTextField)tblUSRRT.cmpEDITR[2]).getText().length()>0)
					((JTextField)tblUSRRT.cmpEDITR[2]).setEditable(false);
				else
					((JTextField)tblUSRRT.cmpEDITR[2]).setEditable(true);
			}
		}catch(Exception e)
			{setMSG(e,"Error in child FocusLost");}
	}
	/**
	 * @param L_AE
	 * <p>Tasks:</p>
	 * <p>IF Source==cl_dat.M_cmbOPTN_pbst</p>
	 * <p>This calls the getDATA method which in turn create Tables and display data in the
	 *  Tables</p>  
	 * <p>IF Source==btnRTDTL</p>
	 * <p>It display the Right detais of the selected User Type in the table for User Access right details.
	 *	It selects data from SA_PPRTR table where user type is equal to the type selected in the table
	 *	and display data in right detail table.</p>	*/
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC==cl_dat.M_cmbOPTN_pbst&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			getDATA();
		/**SHOWS THE RIGHT DETAILS OF SELECTED USER TYPE*/
		else if(M_objSOURC==btnRTDTL)
		{
			try
			{
				((cl_JTBL)tblRTDTL).clrTABLE();
				M_strSQLQRY="SELECT * FROM SA_PPRTR WHERE PPR_USRTP='"+tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),tblUSRRT.getSelectedColumn()).toString().trim()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					int j=0;
					Boolean TRUE=new Boolean(true),FALSE=new Boolean(false);
					while(M_rstRSSET.next())
					{
						tblRTDTL.setValueAt(M_rstRSSET.getString("PPR_USRTP"),j,1);
						tblRTDTL.setValueAt(M_rstRSSET.getString("PPR_SYSCD"),j,2);
						tblRTDTL.setValueAt(M_rstRSSET.getString("PPR_SBSCD"),j,3);
						tblRTDTL.setValueAt(M_rstRSSET.getString("PPR_PRGCD"),j,4);
						tblRTDTL.setValueAt((M_rstRSSET.getString("PPR_ADDFL").equals("Y")? TRUE : FALSE) ,j,5);
						tblRTDTL.setValueAt((M_rstRSSET.getString("PPR_MODFL").equals("Y")? TRUE : FALSE) ,j,6);
						tblRTDTL.setValueAt((M_rstRSSET.getString("PPR_DELFL").equals("Y")? TRUE : FALSE) ,j,7);
						tblRTDTL.setValueAt((M_rstRSSET.getString("PPR_ENQFL").equals("Y")? TRUE : FALSE) ,j++,8);
					}
				}
			}catch(Exception e)
			{setMSG(e," RTDTL:");}
		}
	}
	
	/**DISPLAY HELPSCREEN ON THE F1 KEY PRESS.
	 * @param L_KE
	 * <p>if L_SRC==cmbSYSCD[L_intCOLNO]</p>
	 * <p>if key source is Combo box of Selected system then it checks for operation ADD, MOD OR DEL
	 * L_intCOLNO is local integer variable which contains the column no of selected system
	 * then it checks for whether cmbOPTN equals to Addition or Modification
	 * <p>if cmbOPTN = addition then there is no help on F1 provided to already created users you can create new users only</p>
	 * if user code is available in SA_USMST table then no F1 under Addition</p>
	 * <p>if cmbOPTN = modification then there is no help on F1 provided if any one tries to create new user
	 *  if user code is not available in SA_USMST then no F1 under modification</p>  
	 * <p>if Employee code in tblUSRRT = help on Emplyee code and name from HR_EPMST */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		boolean flgSETFLG;
		try
		{
			if(M_objSOURC == tblUSRRT.cmpEDITR[1] && L_KE.getKeyCode()==L_KE.VK_F1)
			{
				M_strHLPFLD = "EMPCD";
				cl_hlp("Select EP_FULNM,EP_EMPNO,EP_SHRNM,EP_ADRPR from HR_EPMST order by EP_FULNM",1,2,new String[] {"Emp. Name","Employee Code"},4,"CT");
			}
			for(int L_intCOLNO=0;L_intCOLNO<vtrSYSCD.size();L_intCOLNO++)
			{
				if(M_objSOURC==cmbSYSCD[L_intCOLNO]&&L_KE.getKeyCode()==L_KE.VK_F1)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{	
						flgSETFLG=true;
						for(int z=0;z<strUSRCD.length;z++)
							if(tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2).toString().equals(strUSRCD[z]))
								flgSETFLG=false;						   
						if(flgSETFLG)
						{
							if(tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2).toString().length()>0&&tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),3).toString().length()>0&&tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),4).toString().length()>0)
							{
								M_strSQLQRY="SELECT DISTINCT PPR_USRTP,PPR_SYSCD FROM SA_PPRTR WHERE PPR_SYSCD='"+vtrSYSCD.elementAt(tblUSRRT.getSelectedColumn()-5).toString()+"' ORDER BY PPR_USRTP";
								M_strHLPFLD = "cmbSYSCD|"+Integer.toString(L_intCOLNO);
								cl_hlp(M_strSQLQRY,1,1,new String[] {"Sys. Code","User Types"},2,"CT");
								break;
							}
						}
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						flgSETFLG=false;
						for(int z=0;z<strUSRCD.length;z++)
							if(tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2).toString().equals(strUSRCD[z]))
								flgSETFLG=true;						   
						if(flgSETFLG)
						{
							if(tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2).toString().length()>0&&tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),3).toString().length()>0&&tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),4).toString().length()>0)
							{
								M_strSQLQRY="SELECT DISTINCT PPR_USRTP,PPR_SYSCD FROM SA_PPRTR WHERE PPR_SYSCD='"+vtrSYSCD.elementAt(tblUSRRT.getSelectedColumn()-5).toString()+"' ORDER BY PPR_USRTP";
								M_strHLPFLD = "cmbSYSCD|"+Integer.toString(L_intCOLNO);
								cl_hlp(M_strSQLQRY,1,1,new String[] {"Sys. Code","User Types"},2,"CT");
								break;
							}
						}
					}
				}
			}
		}catch(Exception E)
		{			setMSG(E,"Child.key pressed");		}
	}
	
	/**ADD,MODIFY AND DELETE DATA.*/	
	void exeSAVE()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				boolean flgSETFLG=true;
				for(int i=0;i<vtrADMOD.size();i++)
				{
					M_strSQLQRY="SELECT COUNT (*) FROM SA_USMST";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					M_rstRSSET.next();
					String[] L_strUSRCD=new String[Integer.parseInt(M_rstRSSET.getString(1))];
					M_strSQLQRY="SELECT DISTINCT US_USRCD FROM SA_USMST";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					int z=0;
					if(M_rstRSSET!=null)
						while(M_rstRSSET.next())
							L_strUSRCD[z++]=M_rstRSSET.getString("US_USRCD");
					String[] L_strTMPSTR=new String[3];
					StringTokenizer L_strSTRTKN=new StringTokenizer((String)vtrADMOD.elementAt(i),"|");//**
					L_strTMPSTR[0]=L_strSTRTKN.nextToken();//**user code
					L_strTMPSTR[1]=L_strSTRTKN.nextToken();//**system code
					L_strTMPSTR[2]=L_strSTRTKN.nextToken();//**user type
					
					flgSETFLG=true;
					
					for(z=0;z<L_strUSRCD.length&&L_strUSRCD[z]!=null;z++)
					{	
						if(L_strUSRCD[z].equalsIgnoreCase(L_strTMPSTR[0]))//user code
						{
							flgSETFLG=false;/**IF THIS FLAG IS TRUE THEN ONLY UPDATE SA_USMST TABLE.*/
							cl_dat.M_flgLCUPD_pbst=false;/**IF THIS FLAG IS TRUE THEN ONLY UPDATE SA_USTRN TABLE.*/
						}
					}
					if(flgSETFLG)
					{
						for(int j=0;j<tblUSRRT.getRowCount();j++)
						{
							if(tblUSRRT.getValueAt(j,0).equals(new Boolean(true))&&tblUSRRT.getValueAt(j,2).equals(L_strTMPSTR[0])&&L_strTMPSTR[2]!=null)//**checked && user type selected
							{
								cl_dat.M_flgLCUPD_pbst=true;
								
								M_strSQLQRY="INSERT INTO SA_USMST (US_USRCD,US_USRNM,US_USRPW,US_PWMFL,US_EMLRF,US_EMPCD,"
									+"US_DSBID,US_TRNFL,US_STSFL,US_LUSBY,US_LUPDT) VALUES ("
									+"'"+L_strTMPSTR[0].toString().trim().toUpperCase()+"',"//**US_USRCD
									+"'"+tblUSRRT.getValueAt(j,3).toString().trim()+"',"//**US_USRNM
									+"'"+cl_ProcessPassword.encode(L_strTMPSTR[0].toUpperCase())+"',"//**US_USRPW
									+"'Y',"//**US_PWMFL
									+"'"+tblUSRRT.getValueAt(j,4).toString().trim().toLowerCase()+"',"//US_EMLRF
									+"'"+tblUSRRT.getValueAt(j,1).toString().trim()+"',"//US_EMPCD
									+"'"+"',"//**US_DBSID
									+getUSGDTL("US",'i',"0")+")";//US_USTRN,US_STSFL,US_LUSBY,US_LUPDT,
								cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
								
								if(cl_dat.M_flgLCUPD_pbst)
									cl_dat.exeDBCMT("exeSAVE");
							}
						}
					}
				}//end for loop
				
				for(int i=0;i<vtrADMOD.size();i++)
				{
					String[] L_strTMPSTR=new String[3];
					StringTokenizer L_strSTRTKN=new StringTokenizer((String)vtrADMOD.elementAt(i),"|");//**
					L_strTMPSTR[0]=L_strSTRTKN.nextToken();//**user code
					L_strTMPSTR[1]=L_strSTRTKN.nextToken();//**sys code
					L_strTMPSTR[2]=L_strSTRTKN.nextToken();//**user type
					if(L_strTMPSTR[0]!=null&&L_strTMPSTR[1]!=null&&L_strTMPSTR[2]!=null)
					{
						cl_dat.M_flgLCUPD_pbst=true;
						
						M_strSQLQRY="INSERT INTO SA_USTRN (UST_USRCD,UST_SYSCD,UST_USRTP) VALUES ( "
							+"'"+L_strTMPSTR[0].toString().trim().toUpperCase()+"',"//**UST_USRCD
							+"'"+L_strTMPSTR[1].toString().trim().toUpperCase()+"',"//**UST_SYSCD
							+"'"+L_strTMPSTR[2].toString().trim().toUpperCase()+"')";//**UST_USRTP
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						
						if(cl_dat.M_flgLCUPD_pbst)
							cl_dat.exeDBCMT("exeSAVE");
					}
				
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
					boolean flgSETFLG=true;
					for(int i=0;i<tblUSRRT.getRowCount();i++)
					{
						if(tblUSRRT.getValueAt(i,0).equals(Boolean.FALSE))
							continue;
						if(tblUSRRT.getValueAt(i,2).toString().length()==0)
							break;
						M_strSQLQRY="update sa_usmst set "
							+" US_USRNM='"+tblUSRRT.getValueAt(i,3)+"',"//US_USRNM
							+" US_EMLRF='"+tblUSRRT.getValueAt(i,4)+"',"//US_EMLRF
							+" US_EMPCD='"+tblUSRRT.getValueAt(i,1)+"'"//US_EMPCD
							+" where US_USRCD='"+tblUSRRT.getValueAt(i,2)+"'";//US_USRCD
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					
					for(int i=0;i<vtrADMOD.size();i++)
					{
						String[] L_strTMPSTR=new String[3];
						StringTokenizer L_strSTRTKN=new StringTokenizer((String)vtrADMOD.elementAt(i),"|");//**
						L_strTMPSTR[0]=L_strSTRTKN.nextToken();//**user code
						L_strTMPSTR[1]=L_strSTRTKN.nextToken();//**sys code
						L_strTMPSTR[2]=L_strSTRTKN.nextToken();//**user type
						if(L_strTMPSTR[0]!=null&&L_strTMPSTR[1]!=null&&L_strTMPSTR[2]!=null&&cl_dat.M_flgLCUPD_pbst)
						{
							M_strSQLQRY="INSERT INTO SA_USTRN (UST_USRCD,UST_SYSCD,UST_USRTP) VALUES ( "
								+"'"+L_strTMPSTR[0].toString().trim().toUpperCase()+"',"//**UST_USRCD
								+"'"+L_strTMPSTR[1].toString().trim().toUpperCase()+"',"//**UST_SYSCD
								+"'"+L_strTMPSTR[2].toString().trim().toUpperCase()+"')";//**UST_USRTP
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						}
						if(cl_dat.exeDBCMT("exeSAVE"))
							setMSG("User Modified ..",'N');
						else
							setMSG("Modification failed ..",'E');
					}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				for(int j=0;j<tblUSRRT.getRowCount();j++)
				{
					if(tblUSRRT.getValueAt(j,0).equals(new Boolean(true)))
					{
						cl_dat.M_flgLCUPD_pbst=true;
						M_strSQLQRY="DELETE FROM SA_USMST WHERE US_USRCD='"+tblUSRRT.getValueAt(j,2).toString().trim().toUpperCase()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						if(cl_dat.M_flgLCUPD_pbst)
						{
							M_strSQLQRY="DELETE FROM SA_USTRN WHERE UST_USRCD='"+tblUSRRT.getValueAt(j,2).toString().trim().toUpperCase()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						}
					}
					if(cl_dat.M_flgLCUPD_pbst)
						cl_dat.exeDBCMT("exeSAVE");
				}		
			}
			tblUSRRT.clrTABLE();
			getDATA();
			updateUI();
		}catch (Exception e)
				{setMSG(e,"Child.exeSAVE : ");}
	}	


	/**ADDS THE USER TYPE SELECTED FROM HELP SCREEN INTO THE COMBOBOX   
	OF A SELECTED SYSTEM AS WELL AS VECTOR AND HASH TABLE WHEN F1 IS PRESSED.*/
	public void exeHLPOK()
	{
		try
		{
			if(M_strHLPFLD.equals("EMPCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				cl_dat.M_wndHLP_pbst=null;
				StringTokenizer L_strSTRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_strSTRTKN.nextToken();
				((JTextField)tblUSRRT.cmpEDITR[1]).setText(L_strSTRTKN.nextToken());
				tblUSRRT.setValueAt(((JTextField)tblUSRRT.cmpEDITR[1]).getText(),tblUSRRT.getSelectedRow(),1);
			}
			else if(M_strHLPFLD.substring(0,8).equals("cmbSYSCD"))
			{
				boolean flgITEM=true;
				StringTokenizer L_strSTRTKN=new StringTokenizer(M_strHLPFLD,"|");
				L_strSTRTKN.nextToken();
				
				int L_intCOLNO=Integer.parseInt(L_strSTRTKN.nextToken());/**COLUMN NO OF PARTICULAR SYSTEM SELECTED ON F1*/
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				for(int i=1;i<cmbSYSCD[L_intCOLNO].getItemCount();i++)
				{
					if(cmbSYSCD[L_intCOLNO].getItemAt(i).toString().equalsIgnoreCase(cl_dat.M_strHLPSTR_pbst))
					{
						flgITEM=false;
						setMSG("User has Access to this User type",'E');
						JOptionPane.showMessageDialog(null,cmbSYSCD[L_intCOLNO].getItemAt(i).toString(),"User has Access to this User type",JOptionPane.ERROR_MESSAGE);
						requestFocus();
					}
				}
				/**ADDING USER TYPE IN COMBOBOX AS WELL AS ADDING USER CODE, USER NAME,
				USER TYPE IN VECTOR IF flgSETFLG=TRUE*/
				if(flgITEM)
				{
					cmbSYSCD[L_intCOLNO].addItem(cl_dat.M_strHLPSTR_pbst);
					vtrADMOD.addElement(tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2)+"|"+vtrSYSCD.elementAt(L_intCOLNO).toString().toUpperCase()+"|"+cl_dat.M_strHLPSTR_pbst);
					updateUI();
					
					/**PUTTING ARRAY OF USER TYPE IN HASH TABLE FROM WHICH DATA WILL BE 
					DATA WILL BE SELECTED ON THE FOCUSGAIN OF EACH COMBO BOX.*/
					String L_strSTRTMP[]=new String[cmbSYSCD[L_intCOLNO].getItemCount()-1];
					for(int i=1;i<cmbSYSCD[L_intCOLNO].getItemCount();i++)
						L_strSTRTMP[i-1]=cmbSYSCD[L_intCOLNO].getItemAt(i).toString();
					hstUSRTP.put(vtrSYSCD.elementAt(L_intCOLNO).toString().toUpperCase()+"|"+tblUSRRT.getValueAt(tblUSRRT.getSelectedRow(),2).toString().toUpperCase(),L_strSTRTMP);
				}
			}
		}catch(Exception L_EX)
		{			setMSG(L_EX,"Child.exeHLPOK ");		}
	}
	private class TBLINPVF extends TableInputVerifier implements ActionListener
	{
		private JCheckBox L_chbTEMP;
		private cl_JTable L_tblTEMP;
		private Vector<String> L_vtrUSRRT;
		public boolean verify(int P_intROWID, int P_intCOLID)
		{
			try
			{
				if(P_intCOLID == 1)
				{
					if(((JTextField)tblUSRRT.cmpEDITR[1]).getText().length()>0)
					{
						M_rstRSSET=cl_dat.exeSQLQRY0("Select EP_SHRNM,EP_FULNM,EP_ADRPR from HR_EPMST where EP_EMPNO='"+((JTextField)tblUSRRT.cmpEDITR[1]).getText()+"'");
						if(M_rstRSSET!=null)
							if(M_rstRSSET.next())
							{
								tblUSRRT.setValueAt(M_rstRSSET.getString("EP_SHRNM"),P_intROWID,2);
								tblUSRRT.setValueAt((M_rstRSSET.getString("EP_FULNM").length()>32 ? M_rstRSSET.getString("EP_FULNM").replace('|',' ').substring(0,30) : M_rstRSSET.getString("EP_FULNM").replace('|',' ')),P_intROWID,3);
								StringTokenizer L_stkTEMP=new StringTokenizer(M_rstRSSET.getString("EP_ADRPR"),"|");
								String L_strTEMP=null;
								while(L_stkTEMP.hasMoreTokens())
									L_strTEMP=L_stkTEMP.nextToken();
								if(L_strTEMP.indexOf("@",0)>-1)
									tblUSRRT.setValueAt(L_strTEMP,P_intROWID,4);
								return true;
							}
					
					}
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst) && P_intCOLID == 0)
				{
					M_rstRSSET=cl_dat.exeSQLQRY0("Select UST_SYSCD,UST_USRTP from SA_USTRN where UST_USRCD='"+tblUSRRT.getValueAt(P_intROWID,2)+"' order by UST_SYSCD");
					L_vtrUSRRT=new Vector<String>(10,5);
					if(M_rstRSSET!=null)
						while(M_rstRSSET.next())
							L_vtrUSRRT.addElement(M_rstRSSET.getString("UST_SYSCD")+"|"+M_rstRSSET.getString("UST_USRTP"));
					JPanel L_pnlTEMP=new JPanel(null);
					L_pnlTEMP.setSize(100,200);
					L_pnlTEMP.setPreferredSize(new Dimension(150,250));
					L_tblTEMP=crtTBLPNL1(L_pnlTEMP,new String[]{"FL","System","User Type"},L_vtrUSRRT.size(),2,1,7,2,new int[]{50,70,100},new int[]{0});
					StringTokenizer L_stkTEMP=null;
					for(int i=0;i<L_vtrUSRRT.size();i++)
					{
						L_stkTEMP=new StringTokenizer((String)L_vtrUSRRT.elementAt(i),"|");
						L_tblTEMP.setValueAt(L_stkTEMP.nextToken(),i,1);
						L_tblTEMP.setValueAt(L_stkTEMP.nextToken(),i,2);
					}
					L_chbTEMP=new JCheckBox("Select All");
					L_chbTEMP.addActionListener(this);
					add(L_chbTEMP,1,1,1,1,L_pnlTEMP,'L');
					int L_intOPTN=JOptionPane.showConfirmDialog( null,L_pnlTEMP,"Select User Types to delete",JOptionPane.OK_CANCEL_OPTION);
					if (L_intOPTN == 0)
					{
						L_chbTEMP.setSelected(true);
						for(int i=0;i<L_tblTEMP.getRowCount();i++)
							if(L_tblTEMP.getValueAt(i,0).equals(Boolean.FALSE))
							{
								L_chbTEMP.setSelected(false);
								break;
							}
						if(L_chbTEMP.isSelected())
						{
							cl_dat.M_flgLCUPD_pbst=true;
							M_strSQLQRY="DELETE FROM SA_USMST WHERE US_USRCD='"+tblUSRRT.getValueAt(P_intROWID,2).toString().trim().toUpperCase()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
							if(cl_dat.M_flgLCUPD_pbst)
							{
								M_strSQLQRY="DELETE FROM SA_USTRN WHERE UST_USRCD='"+tblUSRRT.getValueAt(P_intROWID,2).toString().trim().toUpperCase()+"'";
								cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
							}
							if(cl_dat.M_flgLCUPD_pbst)
								cl_dat.exeDBCMT("exeSAVE");
						}
						else
						{
							cl_dat.M_flgLCUPD_pbst=true;
							for(int i=0;i<L_tblTEMP.getRowCount() && cl_dat.M_flgLCUPD_pbst;i++)
							{
								if(L_tblTEMP.getValueAt(i,0).equals(Boolean.TRUE))
								{
									M_strSQLQRY="Delete from SA_USTRN where UST_USRCD='"+tblUSRRT.getValueAt(P_intROWID,2)+"' and UST_SYSCD='"+L_tblTEMP.getValueAt(i,1)+"' and UST_USRTP='"+L_tblTEMP.getValueAt(i,2)+"'";
									cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
								}
							}
							if(cl_dat.exeDBCMT("exeDEL"))
								setMSG("User Modified ..",'N');
							else
								setMSG("Error Occured ..",'E');
						}
					}
					else
						tblUSRRT.setValueAt(Boolean.FALSE,P_intROWID,P_intCOLID);
				}
			}catch(Exception e)
			{setMSG(e,"TBLINPVF");
			return false;}
			return true;
		}
		public void actionPerformed(ActionEvent L_AE)
		{
	
			if(M_objSOURC == L_chbTEMP)
			{
				if(L_chbTEMP.isSelected())
				{
					for(int i=0;i<L_tblTEMP.getRowCount();i++)
						L_tblTEMP.setValueAt(Boolean.TRUE,i,0);
				}
			}
		}
	}
	
}