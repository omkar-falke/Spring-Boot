/**
System Name   : System Administration
Program Name  : File Transfer
Program Desc. : Form for transfering files from and to executable directory
				
Author        : AAP
Date          : 03/09/2003
Version       : SA 2.0

Modificaitons 

Modified By    : 
Modified Date  : 
Modified det.  : 
Version        : SA 3.0
*/
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
import java.awt.Dimension; 
class sa_filtr extends cl_pbase 
{/** Reference to file to be transfered */
	private File FILE;/** Final string array for list of base classes */
	private static final String[] staBASCL_fn=new String[]{"FR_LOG","CL_PBASE","CL_DAT","CL_JTABLE","CL_RBASE","CL_EML","CL_COMBO"};/** Pnale for reports */
	private JPanel pnlREPORT;/** File chooser dialog */
	private JFileChooser fchFILES;/** Text field to enter fiel to be trnasfered  */
	private TxtLimit	txtFILNM;/** Textfield for programmer name */
	private TxtLimit	txtPMRNM;/** Text field for remark/description of maintainence job */
	private TxtLimit	txtREMRK;/** Text field for executable path */
	private JTextField	txtEXPTH;/** Text field for programmer path */
	private JTextField	txtPGPTH;/** Text field for source path */
	private JTextField	txtSRPTH;/** Text Feild for system code entry during first upload */
	private JTextField	txtPPSYSCD;/** Text Feild for Program code entry during first upload */
	private JTextField	txtPPPRGCD;/** Text Feild for program description entry during first upload */
	private JTextField	txtPPPRGDS;/** Text Feild for Developed by entry during first upload */
	private JTextField	txtPPDEVBY;/** Text Feild for version no. entry during first upload */
	private JTextField	txtPPVERNO;/** Text Feild for Program location (01 : site; 02 : H.O.) entry during first upload */
	private JTextField	txtPRGLC;/** Text Feild for Program type (ME/TE/RP/QR/HS) entry during first upload */
	private JTextField	txtPRGTP;/** Text Feild for Program header entry during first upload */
	private JTextField	txtPRGHD;/** Radiobutton for FROM EXECUTABLE */
	private JRadioButton	rdbFMEXEC;/** Radiobutton for TO EXECUTABLE */
	private JRadioButton	rdbTOEXEC;
	private ButtonGroup		btg;/** button for First UPLOAD - YES */
	private JButton			btnFUPLDY;/** button for First UPLOAD - NO */
	private JButton			btnFUPLDN;/** button for DESCRIPANCY REPORT */
	private JButton			btnDSCRP;/** button for PEDING JOBS */
	private JButton			btnPDGRP;/** button for JOB LOG */
	private JButton			btnMODRP;/** Global string for file name  */
	private String strFILNM;
	private long LM_LUPTM;
	/**To construct the form	 */
	sa_filtr()
	{
		super(2);
		fchFILES=new JFileChooser("c:\\splerp");
		fchFILES.addActionListener(this);
		txtFILNM=new TxtLimit(100);txtEXPTH=new JTextField();txtPGPTH=new JTextField();txtSRPTH=new JTextField();
		txtPMRNM=new TxtLimit(3);txtREMRK=new TxtLimit(30);
		rdbFMEXEC=new JRadioButton("From Executable");
		rdbTOEXEC=new JRadioButton("To Executable");
		btg=new  ButtonGroup();btg.add(rdbFMEXEC);btg.add(rdbTOEXEC);
		btnDSCRP=new JButton("Descripancy Report");btnPDGRP=new JButton("Pending Job Report");
		btnMODRP=new JButton("Maintainance Report");
		setMatrix(20,4);
		JPanel pnlFILTR=new JPanel(null);
		pnlFILTR.setBorder(BorderFactory.createTitledBorder("File Transfer"));
		add(rdbFMEXEC,1,1,1,1,pnlFILTR,'R');
		add(rdbTOEXEC,2,1,1,1,pnlFILTR,'R');
		add(new JLabel("Executable Path : "),1,2,1,1,pnlFILTR,'L');
		add(txtEXPTH,1,3,1,1.95,pnlFILTR,'L');
		add(new JLabel("Source Path : "),2,2,1,1,pnlFILTR,'L');
		add(txtSRPTH,2,3,1,1.95,pnlFILTR,'L');
		add(new JLabel("Programmer Path : "),3,2,1,1,pnlFILTR,'L');
		add(txtPGPTH,3,3,1,1.95,pnlFILTR,'L');
		add(new JLabel("File Name : "),4,1,1,1,pnlFILTR,'L');
		add(txtFILNM,4,2,1,1,pnlFILTR,'L');
		add(new JLabel("Programmer Initials : "),4,3,1,1,pnlFILTR,'L');
		add(txtPMRNM,4,4,1,0.99,pnlFILTR,'L');
		add(new JLabel("Purpose : "),5,1,1,1,pnlFILTR,'L');
		add(txtREMRK,5,2,1,2.95,pnlFILTR,'L');
		add(pnlFILTR,1,1,6,4,this,'L');
		setMatrix(20,3);
		pnlREPORT=new JPanel(null);
		pnlREPORT.setBorder(BorderFactory.createTitledBorder("Reports"));
		add(btnDSCRP,1,1,1,1,pnlREPORT,'L');
		add(btnMODRP,1,2,1,1,pnlREPORT,'L');
		add(btnPDGRP,1,3,1,1,pnlREPORT,'L');
		add(pnlREPORT,7,1,12,3,this,'L');
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(M_objSOURC==txtFILNM&&L_KE.getKeyCode()==L_KE.VK_F1)
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			fchFILES=new JFileChooser(rdbFMEXEC.isSelected() ? txtEXPTH.getText() : txtPGPTH.getText());
			fchFILES.addActionListener(this);
			int i=fchFILES.showOpenDialog(this);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			if(i==0)
			{
				FILE=fchFILES.getSelectedFile();
				String L_STRTMP=FILE.toString().substring(FILE.getParent().length()+1);
				StringTokenizer L_STRTKN=new StringTokenizer(L_STRTMP,".");
				strFILNM=L_STRTKN.nextToken().toUpperCase();
				for(int j=0;j<staBASCL_fn.length;j++)
					if(staBASCL_fn[j].equalsIgnoreCase(strFILNM))
					{
						JPasswordField txtPASWD=new JPasswordField();
						JPanel pnlTEMP=new JPanel(null);pnlTEMP.add(txtPASWD);txtPASWD.setBounds(20,0,60,30);
						int result=JOptionPane.showConfirmDialog(this,pnlTEMP,"Enter Password for base class access",JOptionPane.YES_NO_OPTION);
						if(result!=0||!new String(txtPASWD.getPassword()).equalsIgnoreCase(cl_ProcessPassword.encode("XOZHHYZHV")))
						{
							clrCOMP();
							setENBL(false);
							cl_dat.M_cmbOPTN_pbst.setSelectedIndex(0);
						}
					}
				txtFILNM.setText(FILE.toString());
				if(rdbFMEXEC.isSelected())
					LM_LUPTM=FILE.lastModified()/1000000;
				else
				{
					File CFILE=new File(txtPGPTH.getText()+"\\"+strFILNM+".class");
					LM_LUPTM=CFILE.lastModified()/1000000;
				}
				getDATA();
			}
		}
	}
	
	void getDATA()
	{
		try
		{
			M_strSQLQRY="SELECT * FROM SA_FLTRN WHERE FLT_PRGCD='"+strFILNM+"' AND FLT_RETDT IS NULL";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET!=null)
			{
				if(M_rstRSSET.next())
				{
					if(rdbFMEXEC.isSelected())
					{
						setMSG("File is already issued to : "+M_rstRSSET.getString("FLT_PMRNM"),'E');
						return;
					}
					txtPMRNM.setText(M_rstRSSET.getString("FLT_PMRNM"));
					txtREMRK.setText(M_rstRSSET.getString("FLT_REMRK"));
					if(M_rstRSSET.getLong("FLT_LUPTM")>LM_LUPTM)
					{
						int i=JOptionPane.showConfirmDialog(this,"You are transfering old version of the file ..\nDo you wish to continue?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
						if(i!=0)
							clrCOMP();
					}
				}
				else
				{
					if(rdbTOEXEC.isSelected())
						setMSG("file is not issued ..",'E');
					else
						setMSG("file is not issued ..",'N');
				}
			}
			else
			{
				if(rdbTOEXEC.isSelected())	
					setMSG("File is not issued .. ",'E');
				else
					setMSG("File is not issued .. ",'N');
			}
			if(rdbTOEXEC.isSelected())
			{
				File JFILE=new File(txtPGPTH.getText()+"\\"+strFILNM+".java"),
					 CFILE=new File(txtPGPTH.getText()+"\\"+strFILNM+".class");
				if(JFILE.lastModified()>CFILE.lastModified())
					setMSG("Source file is not compiled ..",'E');
			}
		}catch(Exception e)
		{System.out.println("Error in getDATA : "+e);}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC==btnDSCRP)
			{//CREATING DESCRIPANCY REPORT
				File flEXE=new File(txtEXPTH.getText());
				String[] L_FILES=flEXE.list();//LIST OF FILES IN EXEC FOLDER
				Hashtable<String,String> hstPGDTL=new Hashtable<String,String>(100,0.1f);//HASHTABLE TO STORE DESCRIPANCY DETAILS
				M_strSQLQRY="SELECT DISTINCT FLT_PRGCD,MAX(FLT_LUPTM) FLT_LUPTM FROM SA_FLTRN GROUP BY FLT_PRGCD";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY );
				if(M_rstRSSET!=null)
				{
					if(M_rstRSSET.next())
					{
						File flEXEC=null;
						while(M_rstRSSET.next())
						{
							StringTokenizer L_STRTKN=null;
							for(int i=0;i<L_FILES.length;i++)
							{
							 L_STRTKN=new StringTokenizer(L_FILES[i],".");
							 L_STRTKN=new StringTokenizer(L_FILES[i],".");//TO REMOVE FILE EXTENSION
								if(L_STRTKN.nextToken().equalsIgnoreCase(M_rstRSSET.getString("FLT_prgcd")))
								{//FILE IS PRESENT IN EXEC AND RESPECTIVE RECORD FOUND
									//GETTING LAST MODIFIED VALUE OF EXEC FILE
									long L_EXLUP=new File(txtEXPTH.getText()+"\\"+L_FILES[i]).lastModified()/1000000;
									if(L_EXLUP>M_rstRSSET.getLong("FLT_LUPTM"))//EXEC FILE IS MODIFIED LATTER
									{
										hstPGDTL.put(L_FILES[i],"Illegal transfer to EXEC");
									}
									else if(L_EXLUP<M_rstRSSET.getLong("FLT_LUPTM"))//EXEC FILE IS MODIFIED EARLIER
									{
										hstPGDTL.put(L_FILES[i],"Illegal transfer of older version to EXEC ");
									}
									L_FILES[i]="xxx";//VALUE TO INDICATE RECORD CHECKED.
								}
							}
						}
					}
					for(int i=0;i<L_FILES.length;i++)
						if(!L_FILES[i].equals("xxx"))
						{//FILE IS TRANSFERED TO EXEC FIRST TIME AND WAS ILLEGAL
							hstPGDTL.put(L_FILES[i],"Illegal Upload of file");
						}
					//PUTTING DATA IN TABLE TO SHOW TO USER.
					String[] names=new String[]{"FL","Program","descripancy"};
					int wid[]=new int[]{20,50,400};
					setMatrix(20,4);
					Enumeration L_ENUM=hstPGDTL.keys();
					String L_STRTMP="";
					int i=0;
					pnlREPORT.removeAll();
					cl_JTBL tblDSCDTL=crtTBLPNL(pnlREPORT,names,hstPGDTL.size(),2,1,10,3.9,wid,new int[]{0});
					while(L_ENUM.hasMoreElements())
					{
						L_STRTMP=(String)L_ENUM.nextElement();
						tblDSCDTL.setValueAt(L_STRTMP,i,1);
						tblDSCDTL.setValueAt(hstPGDTL.get( L_STRTMP).toString(),i,2);
						i++;
					}
					setMatrix(20,3);
					btnDSCRP.removeActionListener(this);btnMODRP.removeActionListener(this);btnPDGRP.removeActionListener(this);
					add(btnDSCRP,1,1,1,1,pnlREPORT,'L');
					add(btnMODRP,1,2,1,1,pnlREPORT,'L');
					add(btnPDGRP,1,3,1,1,pnlREPORT,'L');
					setMatrix(20,4);
					add(pnlREPORT,7,1,12,4,this,'L');
					updateUI();			
				}
			}
			else if(M_objSOURC==btnPDGRP)
			{
				M_strSQLQRY="select * from sa_fltrn where FLT_retdt is null";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY );
				Hashtable<String,String> hstPDGRP=new Hashtable<String,String>(10,0.2f);
				if(M_rstRSSET!=null)
					while(M_rstRSSET.next())
						hstPDGRP.put(M_rstRSSET.getString("FLT_prgcd"),nvlSTRVL(M_rstRSSET.getString("FLT_pmrnm"),"Not Available")+"|"+nvlSTRVL(M_rstRSSET.getString("FLT_issdt"),"NONE")+"|"+nvlSTRVL(M_rstRSSET.getString("FLT_remrk"),"None"));
				String[] names=new String[]{"FL","Program","Programer","Given On","Purpose"};
				int wid[]=new int[]{20,50,100,100,100};
				setMatrix(20,4);
				Enumeration L_ENUM=hstPDGRP.keys();
				String L_STRTMP="";
				StringTokenizer L_STRTKN=null;
				int i=0;
				pnlREPORT.removeAll();
				cl_JTBL tblPDGRP=crtTBLPNL(pnlREPORT,names,hstPDGRP.size(),2,1,10,3.9,wid,new int[]{0});
				while(L_ENUM.hasMoreElements())
				{
					L_STRTMP=(String)L_ENUM.nextElement();
					L_STRTKN=new StringTokenizer(((String)hstPDGRP.get(L_STRTMP)),"|");
					tblPDGRP.setValueAt(L_STRTMP,i,1);
					tblPDGRP.setValueAt(L_STRTKN.nextToken(),i,2);
					tblPDGRP.setValueAt(L_STRTKN.nextToken().substring(0,16),i,3);
					tblPDGRP.setValueAt(L_STRTKN.nextToken(),i,4);
					i++;
				}
				setMatrix(20,3);
				btnDSCRP.removeActionListener(this);btnMODRP.removeActionListener(this);btnPDGRP.removeActionListener(this);
				add(btnDSCRP,1,1,1,1,pnlREPORT,'L');
				add(btnMODRP,1,2,1,1,pnlREPORT,'L');
				add(btnPDGRP,1,3,1,1,pnlREPORT,'L');
				setMatrix(20,4);
				add(pnlREPORT,7,1,12,4,this,'L');
				updateUI();			
			}
			else if(M_objSOURC==btnMODRP)
			{
				M_strSQLQRY="select FLT_prgcd,FLT_pmrnm,FLT_issdt,FLT_retdt,FLT_remrk,(FLT_retdt-FLT_issdt) FLT_days from sa_fltrn where FLT_prgcd='"+strFILNM.toUpperCase()+"'";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY );
				Vector<String> vtrMODRP=new Vector<String>(10,2);
				if(M_rstRSSET!=null)
					while(M_rstRSSET.next())
						vtrMODRP.add(M_rstRSSET.getString("FLT_prgcd")+"|"+nvlSTRVL(M_rstRSSET.getString("FLT_pmrnm"),"Not Available")+"|"+nvlSTRVL(M_rstRSSET.getString("FLT_issdt"),"NONE")+"|"+nvlSTRVL(M_rstRSSET.getString("FLT_retdt"),"NONE")+"|"+nvlSTRVL(M_rstRSSET.getString("FLT_days"),"Not completed")+"|"+nvlSTRVL(M_rstRSSET.getString("FLT_remrk"),"None"));
				String[] names=new String[]{"FL","Program","Programer","Given On","Completed on","Time Required","Purpose"};
				int wid[]=new int[]{20,50,75,75,75,75,75};
				setMatrix(20,4);
				String L_STRTMP="";
				StringTokenizer L_STRTKN=null;
				pnlREPORT.removeAll();
				cl_JTBL tblMODRP=crtTBLPNL(pnlREPORT,names,vtrMODRP.size(),2,1,10,3.9,wid,new int[]{0});
				for(int i=0;i<vtrMODRP.size();i++)
				{
					L_STRTKN=new StringTokenizer(((String)vtrMODRP.elementAt(i)),"|");
					tblMODRP.setValueAt(L_STRTKN.nextToken(),i,1);
					tblMODRP.setValueAt(L_STRTKN.nextToken(),i,2);
					L_STRTMP=nvlSTRVL(L_STRTKN.nextToken()," ");
					if(L_STRTMP.length()>16)
						tblMODRP.setValueAt(L_STRTMP.substring(0,16),i,3);  
					else
						tblMODRP.setValueAt(L_STRTMP,i,3);
					L_STRTMP=L_STRTKN.nextToken();
					if(L_STRTMP.length()>16)
						tblMODRP.setValueAt(L_STRTMP.substring(0,16),i,4);  
					else
						tblMODRP.setValueAt(L_STRTMP,i,4);
					tblMODRP.setValueAt(L_STRTKN.nextToken(),i,5);
					tblMODRP.setValueAt(L_STRTKN.nextToken(),i,6);
				}
				setMatrix(20,3);
				btnDSCRP.removeActionListener(this);btnMODRP.removeActionListener(this);btnPDGRP.removeActionListener(this);
				add(btnDSCRP,1,1,1,1,pnlREPORT,'L');
				add(btnMODRP,1,2,1,1,pnlREPORT,'L');
				add(btnPDGRP,1,3,1,1,pnlREPORT,'L');
				setMatrix(20,4);
				add(pnlREPORT,7,1,12,4,this,'L');
				updateUI();			
			}
		}catch(Exception e)
		{			System.out.println("Error in child ActionPerformed :"+e);		}
	}
	
	void exeSAVE()
	{
		try
		{
			File L_filTEMP=new File(txtEXPTH.getText());
			if(!(L_filTEMP.exists() || L_filTEMP.canWrite()))
			{
				setMSG("Invalid EXECUTABLE path ..",'E');
				return;
			}
			L_filTEMP=new File(txtPGPTH.getText());
			if(!(L_filTEMP.exists() || L_filTEMP.canWrite()))
			{
				setMSG("Invalid PROGRAMMER path ..",'E');
				return;
			}
			L_filTEMP=new File(txtSRPTH.getText());
			if(!(L_filTEMP.exists() || L_filTEMP.canWrite()))
			{
				setMSG("Invalid SOURCE path ..",'E');
				return;
			}
			if(rdbTOEXEC.isSelected())
			{
				boolean FLT_UPLOAD=false;
				M_strSQLQRY="select * from sa_fltrn where FLT_prgcd='"+strFILNM.toUpperCase()+"' and FLT_retdt is null";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY );
				if(M_rstRSSET!=null)
				{
					if(M_rstRSSET.next())
					{
						if(!M_rstRSSET.getString("FLT_PMRNM").equalsIgnoreCase(txtPMRNM.getText()))
						{
							setMSG("File is issued to "+M_rstRSSET.getString("FLT_PMRNM"),'E');
							return;
						}
						if(M_rstRSSET.next())
						{
							setMSG("File is issued to another user also ..",'E');
							return;
						}
					}
					else
					{
						int i=JOptionPane.showConfirmDialog(this,"File was not issued ..\nConfirm First upload","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
						if(i!=0)
							return;
						else
							FLT_UPLOAD=true;
					}
				}
				if(!FLT_UPLOAD)
				{
					M_strSQLQRY="update sa_fltrn set FLT_retdt=current_timestamp,"
						+" FLT_remrk='"+txtREMRK.getText()+"',FLT_LUPTM="+Long.toString( LM_LUPTM)
						+",FLT_lupdt=current_date where FLT_prgcd='"+strFILNM+"' and FLT_retdt is null";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else
				{
					JDialog wndPGDTL=new JDialog();
					txtPPSYSCD=new JTextField(20);
					txtPRGLC=new JTextField(20);
					txtPPPRGCD=new JTextField(20);
					txtPPPRGDS=new JTextField(20);txtPPDEVBY=new JTextField(20);
					txtPPVERNO=new JTextField(20);
					txtPRGTP=new JTextField(20);
					txtPRGHD=new JTextField(20);
					JPanel pnlTEMP=new JPanel();
					pnlTEMP.add(new JLabel("System Code                : "));
					pnlTEMP.add(txtPPSYSCD);
					pnlTEMP.add(new JLabel("Program Code             : "));
					pnlTEMP.add(txtPPPRGCD);
					pnlTEMP.add(new JLabel("Program Description : "));
					pnlTEMP.add(txtPPPRGDS);
					pnlTEMP.add(new JLabel("Developed By             : "));
					pnlTEMP.add(txtPPDEVBY);
					pnlTEMP.add(new JLabel("Version Number          : "));
					pnlTEMP.add(txtPPVERNO);
					pnlTEMP.add(new JLabel("Program Type            : "));
					pnlTEMP.add(txtPRGTP);
					pnlTEMP.add(new JLabel("Program Header         : "));
					pnlTEMP.add(txtPRGHD);
					pnlTEMP.add(new JLabel("Program Location         : "));
					pnlTEMP.add(txtPRGLC);
					pnlTEMP.setSize(400,500);
					pnlTEMP.setPreferredSize(new Dimension(400,500));
					int L_intOPTN=JOptionPane.showConfirmDialog(this,pnlTEMP,"Enter Program details",JOptionPane.YES_NO_OPTION);
					if(L_intOPTN!=0)
						return;
					if(txtPPSYSCD.getText().length()==0||
						txtPPPRGCD.getText().length()==0||
						txtPPPRGDS.getText().length()==0||
					    txtPPDEVBY.getText().length()==0||
						txtPPVERNO.getText().length()==0||
						txtPRGTP.getText().length()==0||
						txtPRGHD.getText().length()==0)
					{
						JOptionPane.showMessageDialog(this,"Unsuffitient details of the program..\nData not saved.");
						return;
					}
						
					M_strSQLQRY="insert into sa_fltrn (FLT_prgcd,FLT_pmrnm,FLT_issdt,FLT_remrk,FLT_retdt,FLT_luptm,FLT_lupdt) values ( "
						+"'"+strFILNM.toUpperCase()+"',"//FLT_prgnm,
						+"'"+txtPMRNM.getText().toUpperCase()+"',"//FLT_pmrnm,
						+"current_timestamp,"//FLT_issdt,
						+"'"+"First upload"+"',"//FLT_remrk,
						+"current_timestamp,"//FLT_issdt,
						+Long.toString(LM_LUPTM)+","//FLT_luptm,
						+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";//FLT_lupdt
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					M_strSQLQRY="insert into sa_PPMST (PP_SYSCD,PP_PRGCD,PP_PRGDS,PP_DEVBY,PP_IMPDT,PP_VERNO,PP_PRGTP,PP_PRGLC,PP_PRGHD,PP_TRNFL,PP_LUSBY,PP_LUPDT ) values ( "
						+"'"+txtPPSYSCD.getText().toUpperCase()+"',"//PP_SYSCD
						+"'"+txtPPPRGCD.getText().toUpperCase()+"',"//PP_PRGCD,,
						+"'"+txtPPPRGDS.getText()+"',"//PP_PRGDS,
						+"'"+txtPPDEVBY.getText().toUpperCase()+"',"//PP_VERNO,
						+"current_date,"//PP_IMPDT
						+"'"+txtPPVERNO.getText()+"',"//PP_VERNO,
						+"'"+txtPRGTP.getText().toUpperCase()+"',"//PP_PRGTP,
						+"'"+txtPRGLC.getText().toUpperCase()+"',"//PP_PRGLC,
						+"'"+txtPRGHD.getText().toUpperCase()+"',"//PP_PRGHD,
						+"'0',"//PP_TRNFL
						+"'"+cl_dat.M_strUSRCD_pbst+"',"//lusby
						+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";//FLT_lupdt
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
			else if(rdbFMEXEC.isSelected())
			{
				M_strSQLQRY="select * from sa_fltrn where FLT_prgcd='"+strFILNM+"' and FLT_retdt is null";
				M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY );
				if(M_rstRSSET!=null)
				{
					if(M_rstRSSET.next())
					{
						setMSG("File is issued to "+M_rstRSSET.getString("FLT_pmrnm"),'E');
						return;
					}
				}
				M_strSQLQRY="insert into sa_fltrn (FLT_prgcd,FLT_pmrnm,FLT_issdt,FLT_remrk,FLT_luptm,FLT_lupdt) values ( "
					+"'"+strFILNM.toUpperCase()+"',"//FLT_prgnm,
					+"'"+txtPMRNM.getText().toUpperCase()+"',"//FLT_pmrnm,
					+"current_timestamp,"//FLT_issdt,
					+"'"+txtREMRK.getText()+"',"//FLT_remrk,
					+Long.toString(LM_LUPTM)+","//FLT_luptm,
					+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";//FLT_lupdt
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			}
			Runtime r = Runtime.getRuntime();
			Process p = null;
			if(rdbFMEXEC.isSelected())
			{
				if(cl_dat.M_flgLCUPD_pbst){
					p  = r.exec("filetr.bat "+txtSRPTH.getText()+"\\"+strFILNM+".java"+" "+txtPGPTH.getText());
					if(p.waitFor()!=0)
					{
						setMSG("File transfer Failed..",'E');
						return;
					}
					p  = r.exec("filetr.bat "+txtEXPTH.getText()+"\\"+strFILNM+"*.class"+" "+txtPGPTH.getText()); 
					if(p.waitFor()!=0)
					{
						setMSG("File transfer Failed..",'E');
						return;
					}
				}
			}
			else if(cl_dat.M_flgLCUPD_pbst)
			{
				
				p  = r.exec("filetr.bat "+txtPGPTH.getText()+"\\"+strFILNM+".java"+" "+txtSRPTH.getText());
				if(p.waitFor()!=0)
				{
					setMSG("File transfer Failed..",'E');
					return;
				}
				p  = r.exec("filetr.bat "+txtPGPTH.getText()+"\\"+strFILNM+"*.class"+" "+txtEXPTH.getText()); 
				if(p.waitFor()!=0)
				{
					setMSG("File transfer Failed..",'E');
					return;
				}
				File JFILE=new File(txtPGPTH.getText()+"\\"+strFILNM+".java");JFILE.delete();
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
				setMSG("File Transfered..",'N');
			else
				setMSG("File transfer Failed..",'E');
		}catch(Exception L_EX){
			setMSG(L_EX,"exeSAVE");
		}
	}
	void clrCOMP()
	{
		super.clrCOMP();
		txtEXPTH.setText("f:\\exec\\splerp3");txtPGPTH.setText("f:\\toho");txtSRPTH.setText("f:\\source\\splerp3");
	}
}
