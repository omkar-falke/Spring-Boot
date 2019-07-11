/*
System Name   : Management Information System
Program Name  : Statement of Stock Position
Program Desc. : 
Author        : N.K.Virdi
Date          : 10 December 2003
Version       : MIS 2.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :
M_flgHELPFL_pbst
*/
// rounding
import java.util.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
//import javax.print;
public class fg_qrstk extends cl_pbase implements MouseListener
{
   
   private JLabel lblTBL1,lblTBL2,lblTBL3,lblCLSST,lblUCLST,lblLTCNT,lblSTKQT,lblSTKUC,lblSTKAV,lblTDSQT;
   private cl_JTBL tblPRDDT,tblTSTDT,tblTDSDT ;
   private JRadioButton rdoDOS,rdoCUS; 
   private ButtonGroup bgrSTK;
   ResultSet LM_RSLSTK,LM_RSLTDS;
   ResultSet LM_RSLRUN,LM_RSLDTL,LM_RSLPRD;
   int intPAGNO,intLINNO;
   int[] LM_COLDEC;
   int L_QPRCNT =11;//colcnt	   
   double LM_BAGQT =0;
   double L_TDSQT =0;
   int LM_REPWID =100;
   int LM_COLWD ;
   int L_RESCNT =0,L_TSTCNT =0;
   final char LM_STKDOS = 'D';
   final char LM_STKCUR = 'C';				  
   char LM_STKOPT ;
   boolean LM_TDSFL;
   String LP_STRQRY;
   String[] LM_ARRHDR = new String[2]; // For Help Header 
   String[] LM_ARRGHD = {" ","Product Type ","Product ","Grade","Product Code"};
   String[] LM_ARRGHD1 = {" ","Lot No.","RCL No.","Stock ","Cl. Status ","DSP","MFI","IZO","VIC","TS","EL","RSM","WI","a","b","Y1"};// Table Header
   int[] LM_ARRGSZ = {40,100,120,120,100}; // Columnm Size in Table    int LM_ROWCNT =50;
   int[] LM_ARRGSZ1 = {20,80,30,60,75,35,35,35,35,35,35,35,35,35,35,35,35}; // Columnm Size in Table    int LM_ROWCNT =50;
   String[] LM_ARRGHD3 = {" ","Lot Number","RCL No.","Stock ","Remark        "};
   int[] LM_ARRGSZ3 = {20,80,20,80,250}; // Columnm Size in Table    int LM_ROWCNT =50;
   String[] LM_QPRCD;
   String LM_TSTTP = "0103"; // composite certification Test           
   String LM_CLSFL ="",L_RMKDS,L_RESNO =""; 
   StringBuffer L_PRNLIN;
	final int TB1_CHKFL =0;
	final int TB1_PRDTP =1;
    final int TB1_PRDCT =2;    
	final int TB1_PRDDS =3;
	final int TB1_PRDCD =4;

	final int TB2_CHKFL =0;
	final int TB2_LOTNO =1;
	final int TB2_RCLNO =2;
	final int TB2_STKQT =3;
	final int TB2_CLSFL =4;
	
	final int TB3_CHKFL =0;
	final int TB3_LOTNO =1;
	final int TB3_RCLNO =2;
	final int TB3_STKQT =3;
	final int TB3_REMDS =4;

	boolean LM_ERRFL = false;
	String LM_RESSTR = "c:\\reports\\FG_RPSTK.HTML"; 
	String LM_PAGE,LM_SUBHEAD,LM_DOCNM,LM_DATE,LM_CMPNM,LM_DPTNM;
    FileOutputStream O_FOUT = createFile(LM_RESSTR);
    DataOutputStream O_DOUT = createDataOutStr(O_FOUT);
    Font f = new Font("Times New Roman",Font.BOLD,14);
	Font f1 = new Font("Times New Roman",Font.BOLD,12);

   fg_qrstk()
	{
	    super(2);
      	int LM_ROWCNT =200;
	  	setMatrix(18,5);
		setVGAP(10);
		add(lblTBL1 = new JLabel("Product Details "),1,1,1,1,this,'L');
		add(new JLabel("Display Options "),1,2,1,1,this,'L');
		add(rdoDOS = new JRadioButton("Day Opening Stock"),1,3,1,1,this,'L');
		add(rdoCUS = new JRadioButton("Current Stock"),1,4,1,1,this,'L');
		bgrSTK = new ButtonGroup();
		lblTBL1.setForeground(Color.blue);
		LM_ROWCNT =6;
		tblPRDDT = crtTBLPNL(this,LM_ARRGHD,LM_ROWCNT,2,1,3,5,LM_ARRGSZ,new int[]{0}) ;	
		add(lblTBL2 = new JLabel("Lot Details "),6,1,1,1,this,'L');
		lblTBL2.setForeground(Color.blue);
		LM_ROWCNT =1000;
		
		tblTSTDT = crtTBLPNL(this,LM_ARRGHD1,LM_ROWCNT,6,1,6,5,LM_ARRGSZ1,new int[]{0}) ;
		tblTSTDT.setEnabled(false);
		add(lblTBL3 = new JLabel("Lot Reservation Details "),8,1,1,1,this,'L');
		lblTBL3.setForeground(Color.blue);
		tblTDSDT = crtTBLPNL(this,LM_ARRGHD3,LM_ROWCNT,13,1,3,3,LM_ARRGSZ3,new int[]{0}) ;
		tblTDSDT.setEnabled(false);
		add(new JLabel("Number Of Lots"),12,4,1,1,this,'L');
		add(new JLabel("Total Stock"),13,4,1,1,this,'L');
		add(new JLabel("Reserved Stock "),14,4,1,1,this,'L');
		add(new JLabel("Available Stock "),15,4,1,1,this,'L');
		add(new JLabel("Unclassified Stock "),16,4,1,1,this,'L');
		
		add(lblLTCNT = new JLabel(""),12,5,1,1,this,'L');
		add(lblSTKQT = new JLabel(""),13,5,1,1,this,'L');
		add(lblTDSQT = new JLabel(" "),14,5,1,1,this,'L');
		add(lblSTKAV = new JLabel(""),15,5,1,1,this,'L');
		add(lblSTKUC = new JLabel(" "),16,5,1,1,this,'L');
		

		lblSTKQT.setForeground(Color.blue);
		lblTDSQT.setForeground(Color.blue);
		lblSTKAV.setForeground(Color.blue);
		lblLTCNT.setForeground(Color.blue);
		lblSTKUC.setForeground(Color.blue);
		tblPRDDT.addMouseListener(this);
		tblTSTDT.addMouseListener(this);
		tblTDSDT.addMouseListener(this);
        exeINTSTA();
		getDTL();
		LM_STKOPT = LM_STKDOS;
  		tblPRDDT.requestFocus();
		tblPRDDT.setRowSelectionInterval(TB1_CHKFL,TB1_CHKFL);
		tblPRDDT.setColumnSelectionInterval(1,2);
	}

    private void exeINTSTA() // execute initial state
	{
		exeCLRTBL1(); 		
		exeCLRTBL2(); 		
		exeCLRTBL3(); 		
 	}

	void exeHLPOK()
    {
         if (M_strHLPFLD.equals("LM_PRDDS"))
         {
			 String L_HLPSTR = getSTRHLP();
			 
             tblPRDDT.setValueAt(cl_dat.M_strHLPSTR_pbst,tblPRDDT.getSelectedRow(),TB1_PRDDS);
			 tblPRDDT.setValueAt(L_HLPSTR.substring(0,L_HLPSTR.indexOf("|")),tblPRDDT.getSelectedRow(),TB1_PRDCD);
	     }
		 else if (M_strHLPFLD.equals("LM_PRDTP"))
         {
			 String L_HLPSTR = getSTRHLP();
		     tblPRDDT.setValueAt(cl_dat.M_strHLPSTR_pbst,tblPRDDT.getSelectedRow(),TB1_PRDTP);
			 tblPRDDT.setValueAt(L_HLPSTR.substring(L_HLPSTR.indexOf("|")+1,L_HLPSTR.length()-1),tblPRDDT.getSelectedRow(),TB1_PRDCT);
	     }
         cl_dat.M_flgHELPFL_pbst = false;
    }
	public void actionPerformed(ActionEvent L_AE)
    {
	    super.actionPerformed(L_AE);
		if (M_objSOURC == cl_dat.M_btnHLPOK_pbst)
           exeHLPOK();
       // else if(M_objSOURC == btnEXIT))
         //  this.dispose();
		/*else if (M_objSOURC == btnPRN)
		{
			if(rdoDOS.isSelected())
				LM_STKOPT =LM_STKDOS;
			else if(rdoCUS.isSelected())
				LM_STKOPT =LM_STKCUR;
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			try
			{
				O_FOUT = createFile(LM_RESSTR);
				O_DOUT = createDataOutStr(O_FOUT); 
				LM_ERRFL = false;
			    getHDDTL();
				getREPDTL();
				O_DOUT.writeBytes("</FONT SIZE =2>");
				O_DOUT.writeBytes("</PRE>");
				O_DOUT.writeBytes("</BODY>");
				O_DOUT.writeBytes("</HTML>");
			}
			catch(Exception L_E)
			{
				System.out.println(L_E.toString());
			}
			exeREP();
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}*/
    }
	public void focusGained(FocusEvent L_FE)
    {
		if(L_FE.getSource().equals(tblPRDDT))
		{
			if(tblPRDDT.getSelectedColumn() == TB1_PRDTP)
				setMSG("press F1 to select the Product Type,click to view the details..",'N');
			else if(tblPRDDT.getSelectedColumn() == TB1_PRDDS)
				setMSG("press F1 to select the Grade..",'N');
			else
				setMSG(" ",'N');
		}
				
    }
   public void keyPressed(KeyEvent L_KE)
    {
	      super.keyPressed(L_KE);
      	  if (L_KE.getKeyCode()== L_KE.VK_ENTER)
          {
			  if(M_objSOURC == rdoDOS)
			  {   
				rdoDOS.setSelected(false);
				rdoCUS.setSelected(true);
				rdoCUS.requestFocus();
				LM_STKOPT = LM_STKCUR;
			  }
			  if(M_objSOURC == rdoCUS)
			  {   
					rdoCUS.transferFocus();
					tblPRDDT.requestFocus();
					tblPRDDT.setRowSelectionInterval(TB1_CHKFL,TB1_CHKFL);
					tblPRDDT.setColumnSelectionInterval(1,2);
			  }
               /*if(M_objSOURC == btnEXIT))
                     this.dispose();*/
               else if (cl_dat.M_flgHELPFL_pbst)
                     exeHLPOK();
			if(M_objSOURC == tblPRDDT)
			{
				if(tblPRDDT.getSelectedColumn() == TB1_PRDTP)
				{
					tblPRDDT.setColumnSelectionInterval(TB1_PRDTP,TB1_PRDDS);	
					tblPRDDT.setRowSelectionInterval(tblPRDDT.getSelectedRow(),tblPRDDT.getSelectedRow());
				    tblPRDDT.editCellAt(tblPRDDT.getSelectedRow(),TB1_PRDTP);
					if(!cl_dat.M_flgHELPFL_pbst)
					{
						if(exePTPVL(tblPRDDT.getValueAt(tblPRDDT.getSelectedRow(),TB1_PRDTP).toString()))
						{
							setMSG("",'N');
						}
						else
						{
							setMSG("Invalid Product Type Category ",'N');
						}
					}
					
				}
				else if(tblPRDDT.getSelectedColumn() == TB1_PRDCT)
				{
					tblPRDDT.setColumnSelectionInterval(TB1_PRDCT,TB1_PRDDS);	
				}
				else if(tblPRDDT.getSelectedColumn() == TB1_PRDDS)
				{
					tblPRDDT.setRowSelectionInterval(tblPRDDT.getSelectedRow(),tblPRDDT.getRowCount());
					tblPRDDT.setColumnSelectionInterval(TB1_PRDTP,TB1_PRDDS);	
					tblPRDDT.editCellAt(tblPRDDT.getSelectedRow(),TB1_PRDTP);
					if(!cl_dat.M_flgHELPFL_pbst)
					{
						if(exePRDVL(tblPRDDT.getValueAt(tblPRDDT.getSelectedRow(),TB1_PRDTP).toString(),tblPRDDT.getValueAt(tblPRDDT.getSelectedRow(),TB1_PRDDS).toString()))
						{
							setMSG("",'N');
						}
						else
						{
							setMSG("Invalid Grade ",'N');
						}
					}
					//tblPRDDT.setColumnSelectionInterval(0,tblPRDDT.getSelectedColumn()+1);	
					
				}
			}
			 
          }
          else if (L_KE.getKeyCode()== L_KE.VK_F1)
          try
          {
			//	char L_DSNTYP = 'M';
			//	int L_SCHCOL = 2;
			//	int L_NUMFLD = 2;
				cl_dat.M_flgHELPFL_pbst = true;
				if(M_objSOURC == tblPRDDT)
				{
					if(tblPRDDT.getSelectedColumn() == TB1_PRDDS)
					{
						cl_dat.M_flgHELPFL_pbst = true;
						M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_STSFL <> 'X' ";
						M_strSQLQRY += " AND PR_PRDTP = "+"'"+tblPRDDT.getValueAt(tblPRDDT.getSelectedRow(),TB1_PRDTP)+"'"+" order by PR_PRDDS ";
						M_strHLPFLD = "LM_PRDDS";
						LM_ARRHDR[0] = " Grade ";
						LM_ARRHDR[1] = " Product Code ";
						int[] L_ARRGSZ = {40,40};
				  		if(M_strSQLQRY != null)
						{
							cl_hlp(M_strSQLQRY,2,2,LM_ARRHDR,2,"CT");
						}	
					}
					if(tblPRDDT.getSelectedColumn() == TB1_PRDTP)
					{
						cl_dat.M_flgHELPFL_pbst = true;
						M_strSQLQRY = "Select CMT_CCSVL,CMT_CODDS from co_cdtrn where CMT_CGMTP = 'MST' and CMT_CGSTP ='COXXSST' and cmt_codcd like 'PR_0%' ";
						M_strHLPFLD = "LM_PRDTP";
						LM_ARRHDR[0] = " Product Type ";
						LM_ARRHDR[1] = " ";
						int[] L_ARRGSZ = {40,40};
				  		if(M_strSQLQRY != null)
						{
							cl_hlp(M_strSQLQRY,2,1,LM_ARRHDR,2,"CT");
						}	
					}
				}
	      }
          catch(NullPointerException L_NPE)
          {
              setMSG("Help not available",'N');                            
          }
          /*else if (L_KE.getKeyCode() == 106)
          {              
              exeINTSTA();
            //  exeDSBSCR();
              btnEXIT.setEnabled(true);
              btnEXIT.requestFocus();
          }*/
		  
			
		  
			  
        }
	
	private void exeCLRTBL1()
	{ 
		for(int i = 0;i<tblPRDDT.getRowCount();i++)
		{
			tblPRDDT.setValueAt(new Boolean(false),i,0);
			for(int j = 1;j<(tblPRDDT.getColumnCount());j++)
			{
					tblPRDDT.setValueAt("",i,j);
			}
		}
	}
	private void exeCLRTBL2()
	{ 
		for(int i = 0;i<tblTSTDT.getRowCount();i++)
		{
			tblTSTDT.setValueAt(new Boolean(false),i,0);
			for(int j = 1;j<(tblTSTDT.getColumnCount());j++)
			{
					tblTSTDT.setValueAt("",i,j);
			}
		}
	}
	private void exeCLRTBL3()
	{ 
		for(int i = 0;i<tblTDSDT.getRowCount();i++)
		{
			tblTDSDT.setValueAt(new Boolean(false),i,0);
			for(int j = 1;j<(tblTDSDT.getColumnCount());j++)
			{
					tblTDSDT.setValueAt("",i,j);
			}
		}
	}
	public void mouseClicked(MouseEvent L_ME)
	{
		int L_TDSCNT =0;
		double L_STKQTY=0;
		double L_TDSQTY=0;
		L_TDSQT =0;
		String L_LOTNO,L_REMDS,L_RCLNO;
		if(M_objSOURC == rdoDOS)
		{
			LM_STKOPT = LM_STKDOS;
		}
		else if(M_objSOURC == rdoCUS)
		{
			LM_STKOPT = LM_STKCUR;
		}
		if(M_objSOURC == tblTSTDT)
		{
			if(tblTSTDT.getSelectedColumn() == 1)
			{
				if(tblTSTDT.getValueAt(tblTSTDT.getSelectedRow(),TB2_LOTNO).toString().length()>0)
				{
					setMSG("Fetching the Classification Details..",'N');
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					getCHKREC("tblTSTDT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else
					setMSG("Please click on a valid Row ..",'E');
			}
		}
		else if(M_objSOURC == tblTDSDT)
		{
			if(tblTDSDT.getSelectedColumn() == 1)
			{
				if(tblTDSDT.getValueAt(tblTDSDT.getSelectedRow(),TB2_LOTNO).toString().length()>0)
				{
					setMSG("Fetching the Classification Details..",'N');
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					getCHKREC("tblTDSDT");		
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else
					setMSG("Please click on a valid Row ..",'E');
			}
		}

		if(M_objSOURC == tblPRDDT)
		{
			if(tblPRDDT.getSelectedColumn() == 1)
			{
				L_RESCNT =0; // initialised
				exeCLRTBL2();
				exeCLRTBL3();
				if(tblPRDDT.getValueAt(tblPRDDT.getSelectedRow(),TB1_PRDDS).toString().length()>0)
				{
					String L_STRSQL="";
					setMSG("Fetching the Lot Details..",'N');
					this.setCursor(cl_dat.M_curWTSTS_pbst);
	  				int grdROWCT = tblPRDDT.getRowCount();
					for(int i = 0;i<=(grdROWCT-1);i++)
					{
							tblPRDDT.setValueAt(new Boolean(false),i,TB1_CHKFL);
							tblPRDDT.setValueAt(new Boolean(true),tblPRDDT.getSelectedRow(),TB1_CHKFL);	
							if(tblPRDDT.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
							{
								exeCLRTBL2();
								exeCLRTBL3();
								this.setCursor(cl_dat.M_curWTSTS_pbst);
								getLOTDTL();		
								this.setCursor(cl_dat.M_curDFSTS_pbst);
							}
					}
				}
		  }
		}
		if(M_objSOURC == tblTDSDT)
		{
			if(tblTDSDT.getSelectedColumn() == TB3_REMDS)
			{
				if(tblTDSDT.getValueAt(tblTDSDT.getSelectedRow(),TB3_REMDS).toString().length()>0)
				{
					//setDSC(tblTDSDT.getValueAt(tblTDSDT.getSelectedRow(),TB3_REMDS).toString());
				}
			}
		}
	}
	public void mousePressed(MouseEvent L_ME){
		
	}
	
	public void mouseReleased(MouseEvent L_ME){
		
	}
	public static DataOutputStream createDataOutStr(FileOutputStream outfile)
 {
		DataOutputStream outStream = new DataOutputStream(outfile);
		return outStream;
 }
 public static FileOutputStream createFile(String strFile)
 {
		FileOutputStream outFile = null;
		try
		{
			File file = new File(strFile);
			outFile = new FileOutputStream(file);
        
			return outFile;
		}
		catch(IOException ex) 
		{
			System.out.println("Error 1");
			return outFile;		
		}
 }
private void getLOTDTL()
{
	String L_QPRVL,L_PRDCD,L_STRSQL;
	int L_CNT =0,L_TDSCNT =0;
	String L_LOTNO,L_STKQT,L_PRDTP,L_RCLNO;
	double L_CLSQT = 0,L_UCLQT=0,L_STKQTY=0,L_SMCLQT=0,L_SMUCQT=0,L_SMTDQT;
	L_RMKDS ="";
	try
	{
		L_STKQTY =0;
		L_SMCLQT=0;
		L_SMUCQT=0;
		L_SMTDQT =0;
	if(LM_STKOPT == LM_STKCUR)
	{
		L_STRSQL = "Select ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RESNO,LT_CLSFL,ST_REMDS,sum(ST_STKQT)L_CLSQT,sum(ST_UCLQT)L_UCLQT from FG_STMST,PR_LTMST where ";
		L_STRSQL += " ST_PRDTP = LT_PRDTP AND ST_LOTNO = LT_LOTNO and ST_RCLNO =LT_RCLNO and(ST_STKQT+ST_UCLQT) > 0 and ST_PRDTP = "+ "'"+ tblPRDDT.getValueAt(tblPRDDT.getSelectedRow(),TB1_PRDTP).toString()+"'";
		
	}
	else // Day opening Stock
	{
		L_STRSQL = "Select ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RESNO,LT_CLSFL,ST_REMDS,sum(ST_DOSQT)L_CLSQT,sum(ST_DOUQT)L_UCLQT from FG_STMST,PR_LTMST where ";
		L_STRSQL += " ST_PRDTP = LT_PRDTP AND ST_LOTNO = LT_LOTNO and ST_RCLNO =LT_RCLNO and(ST_DOSQT+ST_DOUQT) > 0 and ST_PRDTP = "+ "'"+ tblPRDDT.getValueAt(tblPRDDT.getSelectedRow(),TB1_PRDTP).toString()+"'";	
	}
		L_STRSQL += " and ST_PRDCD = "+ "'"+ tblPRDDT.getValueAt(tblPRDDT.getSelectedRow(),TB1_PRDCD).toString() +"'"+ " group by ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RESNO,LT_CLSFL,ST_REMDS Order by ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RESNO";
		LM_RSLSTK = cl_dat.exeSQLQRY1(L_STRSQL);
// commented code modified on 07/10/2002 as classified and unclassified stocks both were getting dispalyed at HO.(as per KVM)
		if(LM_RSLSTK !=null)
		while(LM_RSLSTK.next())
		{
			L_PRDTP = LM_RSLSTK.getString("ST_PRDTP");			
			L_LOTNO = LM_RSLSTK.getString("ST_LOTNO");	
			L_RCLNO = LM_RSLSTK.getString("ST_RCLNO");			
			L_CLSQT = LM_RSLSTK.getDouble("L_CLSQT");			
			L_UCLQT = LM_RSLSTK.getDouble("L_UCLQT");			
			L_RESNO = nvlSTRVL(LM_RSLSTK.getString("ST_RESNO"),"");			
			LM_CLSFL = LM_RSLSTK.getString("LT_CLSFL");			
			L_RMKDS = LM_RSLSTK.getString("ST_REMDS");			
			L_STKQTY = L_CLSQT+L_UCLQT;
			L_SMCLQT += L_CLSQT; // sum of Classified Quantity
			L_SMUCQT += L_UCLQT; // sum of Unclassified Quantity
			if(L_STKQTY > 0 )
			//if(L_CLSQT > 0 )
			{
				tblTSTDT.setValueAt(L_LOTNO,L_CNT,TB2_LOTNO);
				tblTSTDT.setValueAt(L_RCLNO,L_CNT,TB2_RCLNO);
			//	tblTSTDT.setValueAt(setFMT(String.valueOf(L_STKQTY),3),L_CNT,TB2_STKQT);
				tblTSTDT.setValueAt(setFMT(String.valueOf(L_CLSQT),3),L_CNT,TB2_STKQT);
				if(LM_CLSFL !=null)
				{
						if((LM_CLSFL.trim().equals("9"))||(LM_CLSFL.trim().equals("8")))
   							LM_CLSFL ="Classified";
   						else if(LM_CLSFL.trim().equals("4"))
   							LM_CLSFL ="Prov. Classified";
   						else 
   							LM_CLSFL ="Unclassified";
				}
				else
				LM_CLSFL ="";
				tblTSTDT.setValueAt(LM_CLSFL,L_CNT,TB2_CLSFL);
				if(L_RESNO.trim().length()>0)
				{
					if(L_STKQTY > 0 )
					//if(L_CLSQT > 0 )
					{
							tblTDSDT.setValueAt(L_LOTNO,L_TDSCNT,TB3_LOTNO);
							tblTDSDT.setValueAt(L_RCLNO,L_TDSCNT,TB3_RCLNO);
							tblTDSDT.setValueAt(setFMT(String.valueOf(L_CLSQT),3),L_TDSCNT,TB3_STKQT);
							tblTDSDT.setValueAt(L_RMKDS,L_TDSCNT,TB3_REMDS);
							L_TDSCNT++;
							L_SMTDQT +=L_CLSQT;
					}
					LM_TDSFL = true;
				}
				else
					LM_TDSFL = false;
				if(L_RMKDS == null)
					L_RMKDS ="";
				getTSTDT(L_PRDTP,L_LOTNO,L_RCLNO,L_CNT,"QRY");
				L_CNT++;
			}
		}
		lblLTCNT.setText(padSTRING('L',String.valueOf(L_CNT),15));
		lblSTKQT.setText(padSTRING('L',setFMT(String.valueOf(L_SMCLQT+L_SMUCQT),3)+"  MT",16));
		lblSTKUC.setText(padSTRING('L',setFMT(String.valueOf(L_SMUCQT),3)+"  MT",17));
	//	lblSTKAV.setText(padSTRING('L',setFMT(String.valueOf(L_SMCLQT+L_SMUCQT-L_SMTDQT),3)+"  MT",16));
		lblSTKAV.setText(padSTRING('L',setFMT(String.valueOf(L_SMCLQT-L_SMTDQT),3)+"  MT",16));
		lblTDSQT.setText(padSTRING('L',setFMT(String.valueOf(L_SMTDQT),3)+"  MT",17));
	}	
	catch(SQLException L_SE)
	{
		System.out.println("Error 14"+L_SE.toString());
	}
	catch(Exception L_SE)
	{
		System.out.println("Error 15"+L_SE.toString());
	}
}
 private boolean getDTL()
 {
	 int i = 0;
	 ResultSet L_RSLSET ;
	 try
	 {
    	LM_QPRCD = new String[L_QPRCNT]; 
	    LM_COLDEC = new int[L_QPRCNT]; 
		M_strSQLQRY = "Select CMT_CODCD,CMT_NCSVL,CMT_NMP02 from CO_CDTRN where CMT_CGMTP = 'RPT' ";
 		M_strSQLQRY += " AND CMT_CGSTP = 'QCXXPTA'" ;
		M_strSQLQRY += " ORDER BY CMT_NCSVL";
		L_RSLSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(L_RSLSET == null)
		{
			System.out.println("null result set");
			return false;
		}
		else
		{
			while(L_RSLSET.next())
			{
				LM_QPRCD[i] = L_RSLSET.getString("CMT_CODCD");
				if(LM_QPRCD[i]!=null)
				LM_COLDEC[i] = L_RSLSET.getInt("CMT_NMP02");// decimal
				i=i+1;
				
			}
		    L_RSLSET.close();
		}		
	 }
	 catch(SQLException L_SE)
	 {
		 System.out.println("Error 4 "+L_SE.toString());
		 return false;
	 }
	 catch(Exception L_IOE)
	 {
		 System.out.println("Error 5"+L_IOE.toString());
		 return false;
	 }
	 return true;
 }
public void getTSTDT(String LP_PRDTP,String LP_LOTNO,String LP_RCLNO,int LP_CNT,String LP_REPQRY) // Get the Resultset of details, Lot details in a run No. 
{
	L_TSTCNT =0;
	M_strSQLQRY ="";
	String L_QPRVL ="";
	try
	{
		M_strSQLQRY = "Select LT_LOTNO, ";
		int k=0;
		M_strSQLQRY += "PS_"+LM_QPRCD[k].trim()+"VL";
		for(k=1;k<LM_QPRCD.length;k++)
		{
			M_strSQLQRY += ","+"PS_"+LM_QPRCD[k].trim()+"VL";
		}
		M_strSQLQRY +=" FROM QC_PSMST,PR_LTMST where LT_LOTNO = PS_LOTNO AND LT_RCLNO = PS_RCLNO AND";
		M_strSQLQRY +=" PS_QCATP = " + "'"+LP_PRDTP + "'" + " AND PS_TSTTP = " + "'"+ LM_TSTTP + "'";
		M_strSQLQRY += " and PS_LOTNO = "+"'"+LP_LOTNO +"'";
		M_strSQLQRY += " and PS_RCLNO = "+"'"+LP_RCLNO +"'";
		M_strSQLQRY += " AND PS_STSFL <> 'X' ";
		//M_strSQLQRY += " AND PS_LOTNO = LT_LOTNO and PS_LOTNO = "+"'"+LP_LOTNO +"'";
		LM_RSLDTL = cl_dat.exeSQLQRY2(M_strSQLQRY);
 		if(LM_RSLDTL.next())
		{
			L_TSTCNT ++;
			for(int l=0;l<LM_QPRCD.length;l++)
   			{
   				L_QPRVL = "-"; 
   			
   				L_QPRVL = LM_RSLDTL.getString("PS_"+LM_QPRCD[l].trim()+"VL");
   				if(L_QPRVL == null)
   					L_QPRVL = "-";
   				if(LP_REPQRY.equals("QRY"))
				{
					//tblTSTDT.setValueAt(LM_CLSFL,LP_CNT,TB2_CLSFL);
   					tblTSTDT.setValueAt(L_QPRVL,LP_CNT,l+5);
			
				}
				else if(LP_REPQRY.equals("REP"))
				{
					L_PRNLIN.append(padSTRING('L',L_QPRVL,6));
				}
   			}
		}
	}
	catch(Exception L_SE)
	{
		System.out.println("Error "+L_SE.toString());
	}
	
}
private boolean getHDDTL()
 {
	 try
	 {
		LM_SUBHEAD ="";
		cl_dat.M_PAGENO = 1;
	    intLINNO = 0;
	    LM_CMPNM = "SUPREME PETROCHEM LIMITED";
		LM_DPTNM = "FINISHED GOODS INVENTORY MANAGEMENT SYSTEM";
        LM_DOCNM = "Statement of Stock Position as on ";// printing in 12 cpi enhanced + bold
	 	O_DOUT.writeBytes("<HTML>");
		O_DOUT.writeBytes("<HEAD>");
		O_DOUT.writeBytes("</HEAD>");
		O_DOUT.writeBytes("<BODY>");
		O_DOUT.writeBytes("<PRE>");
		O_DOUT.writeBytes("<FONT SIZE=2>");
		LM_SUBHEAD = padSTRING('R',"Lot No.",13);
		LM_SUBHEAD += padSTRING('R',"Stock ",8);
		LM_SUBHEAD += padSTRING('R',"Cls.",4);
		// Data updation date of FIMS data
		//LM_DATE = cl_dat.getPRMCOD("cmt_chp01","DTR","FGXXTOH","0001");
		LM_DATE = cl_dat.getPRMCOD("cmt_ccsvl","S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","DOCDT");
//		LM_DATE = cc_dattm.occ_dattm.getABDATE(LM_DATE,1,'A');
		M_calLOCAL.setTime(M_fmtLCDAT.parse(LM_DATE));
		M_calLOCAL.add(Calendar.DATE,+1);
		LM_DATE = M_fmtLCDAT.format(M_calLOCAL.getTime());
        LM_COLWD = 6;
		for(int j=0;j<L_QPRCNT;j++)
		{
			String LM_DESC = cl_dat.getPRMCOD("cmt_SHRDS","SYS","QCXXQPR",LM_QPRCD[j]);
			if(LM_DESC !=null)
			{
               LM_DESC = LM_DESC.trim();
			   if(j==0)
			   LM_SUBHEAD = LM_SUBHEAD + padSTRING('L',LM_DESC,3);
				else
			   LM_SUBHEAD = LM_SUBHEAD + padSTRING('L',LM_DESC,LM_COLWD);
			}
		}
		LM_SUBHEAD += padSTRING('L',"Remarks",10);
		prnHEAD();
 	 }
	 catch(Exception L_IOE)
	 { 
		 System.out.println("Error 5"+L_IOE.toString());
		 return false;
	 }
	 return true;
 
 }
 private void crtLINE(int LM_CNT)
 {
	String strline = "";
	 try
	{
	 for(int i=1;i<=LM_CNT;i++)
	 {
		 strline += "-";
			
	  }
	 O_DOUT.writeBytes(strline);
	}
	catch(Exception ioe)
	{
			System.out.println("error in change Line");
	}
	 	 
 }
private void prnHEAD()
{
	try
	{
		intLINNO = 0;
		O_DOUT.writeBytes("\n");
		O_DOUT.writeBytes("<Center>");
		O_DOUT.writeBytes("<B>");
		O_DOUT.writeBytes(LM_CMPNM);
		O_DOUT.writeBytes("\n\n");
		O_DOUT.writeBytes(LM_DPTNM);
		O_DOUT.writeBytes("\n\n\n");
		O_DOUT.writeBytes("</Center>");
		O_DOUT.writeBytes (LM_DOCNM + LM_DATE);
		O_DOUT.writeBytes("</B>");
		LM_PAGE = "Page  : " + String.valueOf(cl_dat.M_PAGENO);
		LM_PAGE = padSTRING('L',LM_PAGE,80);
		O_DOUT.writeBytes(LM_PAGE);
		O_DOUT.writeBytes("\n");
		crtLINE(125);
		O_DOUT.writeBytes("\n");	
		O_DOUT.writeBytes(LM_SUBHEAD);
		O_DOUT.writeBytes("\n");
        crtLINE(125);
		O_DOUT.writeBytes("\n");
		intLINNO = 10;

	}
	catch(Exception L_EX)
	{
		System.out.println("Error 10"+L_EX.toString());
		}
	}
public void getREPDTL()
{
	String L_QPRVL,L_PRDCD,L_STRSQL;
	int L_CNT =0;
	String L_LOTNO,L_RCLNO,L_STKQT,L_PRDTP,L_PRVPRD="",L_CNDSTR="";
	double L_CLSQT = 0,L_UCLQT=0,L_STKQTY=0,L_SMCLQT=0,L_SMUCQT=0,L_SMSTQT=0,L_SMTDQT=0;
	try
	{
		L_STKQTY =0;
		L_SMCLQT=0;
		L_SMUCQT=0;
		if(LM_STKOPT == LM_STKCUR)
		{
			L_STRSQL = "Select ST_PRDCD,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RESNO,LT_CLSFL,ST_REMDS,sum(ST_STKQT)L_CLSQT,sum(ST_UCLQT)L_UCLQT from FG_STMST,PR_LTMST where ";
			L_STRSQL += " ST_STKQT+ST_UCLQT >0 and ST_STSFL <> 'X' and ";
	
		}
		else // Day opening Stock
		{
			L_STRSQL = "Select ST_PRDCD,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RESNO,LT_CLSFL,ST_REMDS,sum(ST_DOSQT)L_CLSQT,sum(ST_DOUQT)L_UCLQT from FG_STMST,PR_LTMST where ";
			L_STRSQL += " ST_DOSQT+ST_DOUQT >0 and ";
	
		}
		L_STRSQL += " ST_PRDTP = LT_PRDTP and ST_LOTNO = LT_LOTNO and st_rclno = lt_rclno and ST_PRDTP ||ST_PRDCD IN(";
		for(int i=0;i< tblPRDDT.getRowCount();i++)
		{
			if((tblPRDDT.getValueAt(i,TB1_PRDTP).toString().length()>0)&&(tblPRDDT.getValueAt(i,TB1_PRDCD).toString().length()>0))
			{
				if(i==0)
				L_CNDSTR = "'"+tblPRDDT.getValueAt(i,TB1_PRDTP).toString().trim()+tblPRDDT.getValueAt(i,TB1_PRDCD).toString().trim()+"'";
				else
					L_CNDSTR += ",'"+tblPRDDT.getValueAt(i,TB1_PRDTP).toString().trim()+tblPRDDT.getValueAt(i,TB1_PRDCD).toString().trim()+"'";
			}
		}
		L_CNDSTR +=")";
		L_STRSQL +=L_CNDSTR ;
		L_STRSQL += " group by ST_PRDCD,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_RESNO,LT_CLSFL,ST_REMDS Order by ST_PRDCD,ST_PRDTP,ST_LOTNO,ST_RCLNO";
		LM_RSLSTK = cl_dat.exeSQLQRY1(L_STRSQL);
		if(LM_RSLSTK !=null)
		while(LM_RSLSTK.next())
		{
			L_PRNLIN = new StringBuffer();
			L_PRDCD = LM_RSLSTK.getString("ST_PRDCD");			
			L_PRDTP = LM_RSLSTK.getString("ST_PRDTP");			
			L_LOTNO = LM_RSLSTK.getString("ST_LOTNO");			
			L_RCLNO = LM_RSLSTK.getString("ST_RCLNO");			
			L_RESNO = nvlSTRVL(LM_RSLSTK.getString("ST_RESNO"),"");
			LM_CLSFL = LM_RSLSTK.getString("LT_CLSFL");			
			L_RMKDS = LM_RSLSTK.getString("ST_REMDS");			
			L_CLSQT = LM_RSLSTK.getDouble("L_CLSQT");			
			L_UCLQT = LM_RSLSTK.getDouble("L_UCLQT");			
			if(!L_PRDCD.equals(L_PRVPRD))
			{
				O_DOUT.writeBytes("<B>");
				if(L_PRVPRD.trim().length()>0)
				{
					prnSMRY(L_CNT,L_SMSTQT,L_SMTDQT,L_SMUCQT);
				
					L_SMSTQT = 0;
					L_SMTDQT = 0;
					L_SMUCQT = 0;
					L_SMCLQT = 0;
					L_STKQTY = 0;
					L_CNT    = 0;
				}
				O_DOUT.writeBytes(padSTRING('R',cl_cust.ocl_cust.getPRDDS(L_PRDCD),10));
				O_DOUT.writeBytes("</B>");
				O_DOUT.writeBytes("\n\n");
				intLINNO += 2;
				chkPAGE(intLINNO);
				L_STKQTY = L_CLSQT+L_UCLQT;
				L_SMSTQT +=L_STKQTY;
				L_SMCLQT +=L_CLSQT;
				L_SMUCQT +=L_UCLQT;
				L_PRVPRD = L_PRDCD;
			}
			else
			{
				L_STKQTY = L_CLSQT+L_UCLQT;
				L_SMSTQT +=L_STKQTY;
				L_SMCLQT +=L_CLSQT;
				L_SMUCQT +=L_UCLQT;
				
			}
	
			/////////
			if((LM_CLSFL.trim().equals("9"))||(LM_CLSFL.trim().equals("8")))
   				LM_CLSFL ="C";
   			else if(LM_CLSFL.trim().equals("4"))
   				LM_CLSFL ="P";
   			else 
   				LM_CLSFL ="U";
			if(L_RMKDS == null)
				L_RMKDS =" ";
			////////
			if(L_STKQTY > 0 )
		//	if(L_CLSQT > 0 )
			{
				L_PRNLIN.append(padSTRING('R',L_LOTNO,10));
				L_PRNLIN.append(padSTRING('L',setFMT(String.valueOf(L_CLSQT),3),8));
				L_PRNLIN.append(padSTRING('L',LM_CLSFL,4));	
				getTSTDT(L_PRDTP,L_LOTNO,L_RCLNO,L_CNT,"REP");
				if(L_TSTCNT == 0)
					L_PRNLIN.append(padSTRING('L',"",65));	
				if(L_RESNO.trim().length()>0)
				//	L_SMTDQT += L_STKQTY;
					L_SMTDQT += L_CLSQT;
				O_DOUT.writeBytes(L_PRNLIN.toString());
				O_DOUT.writeBytes("<FONT SIZE=1>");
				O_DOUT.writeBytes(padSTRING('L',"",3));
				O_DOUT.writeBytes(L_RMKDS);	
				O_DOUT.writeBytes("</FONT SIZE=1>");
				O_DOUT.writeBytes("\n");
				intLINNO += 1;
				chkPAGE(intLINNO);
				L_CNT++;
			}
			
		}
		if(L_CNT >0)
		{
			O_DOUT.writeBytes("<B>");
			prnSMRY(L_CNT,L_SMSTQT,L_SMTDQT,L_SMUCQT);
			O_DOUT.writeBytes("</B>");
		}
	}	
	catch(SQLException L_SE)
	{
		System.out.println("Error 14"+L_SE.toString());
	}
	catch(Exception L_SE)
	{
		System.out.println("Error 15"+L_SE.toString());
	}
}
private void exeREP()
 {
	 setMSG("Report Creation in Progress .. please wait",'N');
	 this.setCursor(cl_dat.M_curWTSTS_pbst);
	 try
	{
		if(true)
		{
			setMSG("Report has been created..Please take print in landscape.",'N');
			O_DOUT.close();
			O_FOUT.close();
			Runtime r = Runtime.getRuntime();
            Process p = null;
            try
			{        
				p  = r.exec("c:\\Program Files\\Internet Explorer\\iexplore.exe "+LM_RESSTR); 
             }
            catch(IOException se)
            {
                     System.out.println("Error 7 "+se);
            }
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		 }
		 else
		 {
			O_DOUT.close();
			O_FOUT.close();
			this.setCursor(cl_dat.M_curDFSTS_pbst);	
			LM_ERRFL = true;
        }
	 }
	catch(IOException L_IE)
	{
		System.out.println("error IO");	
	}
	
	}
private void chkPAGE(int L_LINENO)
{
	 try
	{
		 // Landscape printing
		if(L_LINENO > 42)
		{
			crtLINE(125);
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("Note: C: Classified Lots  U: Unclassified Lots  P: Prov. Classified Lots ");
			O_DOUT.writeBytes("\n");
			for(int i=0;i<(51-(L_LINENO+2));i++)
			{
				O_DOUT.writeBytes("\n");
			}
			cl_dat.M_PAGENO += 1;
		    intLINNO = 0;
			prnHEAD();
		}
	
	}
	catch(Exception L_EX)
	{
		System.out.println(L_EX.toString());
	}
}
private boolean exePTPVL(String LP_PRDTP)
{
	try
	{
		M_strSQLQRY = "Select * from co_cdtrn where CMT_CGMTP = 'MST' and CMT_CGSTP ='COXXSST' and cmt_codcd like 'PR_0%' and cmt_ccsvl = '"+LP_PRDTP+"'";
		LM_RSLPRD = cl_dat.exeSQLQRY1(M_strSQLQRY);	
		if(LM_RSLPRD !=null)
			if(LM_RSLPRD.next())
			{
				tblPRDDT.setValueAt(LM_RSLPRD.getString("CMT_CODDS"),tblPRDDT.getSelectedRow(),TB1_PRDCT);
				return true;
			}
			else
			{
				return false;
			}
	}
	catch(Exception L_E)
	{
		System.out.println(L_E.toString());
	 }
	return true;
}
private boolean exePRDVL(String LP_PRDTP,String LP_PRDDS)
{
	try
	{
		M_strSQLQRY = "Select * from CO_PRMST where PR_PRDTP = '"+LP_PRDTP.trim() +"'"+ "AND PR_PRDDS ='"+LP_PRDDS.trim().toUpperCase()+"'";
		LM_RSLPRD = cl_dat.exeSQLQRY1(M_strSQLQRY);	
		if(LM_RSLPRD !=null)
			if(LM_RSLPRD.next())
			{
				tblPRDDT.setValueAt(LM_RSLPRD.getString("PR_PRDCD"),tblPRDDT.getSelectedRow(),TB1_PRDCD);
				return true;
			}
			else
			{
				return false;
			}
	}
	catch(Exception L_E)
	{
		System.out.println(L_E.toString());
	 }
	return true;
}
private void getCHKREC(String LP_TBLNM){
	try{
		int L_ROWCNT =0;
		if(LP_TBLNM.equals("tblTSTDT"))
		{
			L_ROWCNT = tblTSTDT.getRowCount(); 
		}
		else if(LP_TBLNM.equals("tblTDSDT"))
		{
			L_ROWCNT = tblTDSDT.getRowCount(); 
		}	
		for(int i = 0;i<(L_ROWCNT-1);i++)
		   {
			if(LP_TBLNM.equals("tblTSTDT"))
			{
				tblTSTDT.setValueAt(new Boolean(false),i,TB2_CHKFL);
				tblTSTDT.setValueAt(new Boolean(true),tblTSTDT.getSelectedRow(),TB2_CHKFL);	
			/*	if(tblTSTDT.getValueAt(i,TB2_CHKFL).toString().trim().equals("true"))
				{
					qc_qrlcl oqc_qrlcl = new qc_qrlcl();
					oqc_qrlcl.show();
					oqc_qrlcl.exeLTQRY(tblTSTDT.getValueAt(i,TB2_LOTNO).toString().trim(),tblTSTDT.getValueAt(i,TB2_RCLNO).toString().trim(),"01","01");				
			
				}*/
			}
			else if(LP_TBLNM.equals("tblTDSDT"))
			{
				tblTDSDT.setValueAt(new Boolean(false),i,TB3_CHKFL);
				tblTDSDT.setValueAt(new Boolean(true),tblTDSDT.getSelectedRow(),TB3_CHKFL);	
				/*if(tblTDSDT.getValueAt(i,TB3_CHKFL).toString().trim().equals("true"))
				{
					qc_qrlcl oqc_qrlcl = new qc_qrlcl();
					oqc_qrlcl.show();
					oqc_qrlcl.exeLTQRY(tblTDSDT.getValueAt(i,TB3_LOTNO).toString().trim(),tblTDSDT.getValueAt(i,TB3_RCLNO).toString().trim(),"01","01");				
			
				}*/
			}
	 }
	}catch(Exception L_EX){
		System.out.println(L_EX);
		}
	}
private void prnSMRY(int LP_CNT,double LP_SMSTQT,double LP_SMTDQT,double LP_SMUCQT)
{
	try
	{
		O_DOUT.writeBytes("\n");
			intLINNO += 1;
			chkPAGE(intLINNO);
			crtLINE(125);
			O_DOUT.writeBytes("\n");
			intLINNO += 1;
			chkPAGE(intLINNO);
			O_DOUT.writeBytes(padSTRING('R',"Number of Lots : "+String.valueOf(LP_CNT),35));
			O_DOUT.writeBytes(padSTRING('R',"Total Stock       : "+padSTRING('L',setFMT(String.valueOf(LP_SMSTQT),3),12),35));
			O_DOUT.writeBytes(padSTRING('R',"Stock Available    : "+padSTRING('L',setFMT(String.valueOf(LP_SMSTQT-LP_SMTDQT-LP_SMUCQT),3),12),35));
			O_DOUT.writeBytes("\n");
			intLINNO += 1;
			chkPAGE(intLINNO);
			O_DOUT.writeBytes(padSTRING('R'," ",35));
			O_DOUT.writeBytes(padSTRING('R',"Target Despatches : "+padSTRING('L',setFMT(String.valueOf(LP_SMTDQT),3),12),35));
			O_DOUT.writeBytes(padSTRING('R',"Unclassified Stock : "+padSTRING('L',setFMT(String.valueOf(LP_SMUCQT),3),12),35));
			O_DOUT.writeBytes("\n");
			intLINNO += 1;
			chkPAGE(intLINNO);	
			crtLINE(125);
			O_DOUT.writeBytes("\n");
			intLINNO += 1;
			chkPAGE(intLINNO);	
	}
	catch(Exception L_E)
	{
		System.out.println(L_E.toString());
	}

}
}

