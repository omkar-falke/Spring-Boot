/* pROGRAM FOR DATA TRNSFER FROM TEXT FILE TO AS/400 */
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import java.text.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.File;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

class cl_dattr extends cl_pbase
{
	JTextField	txtTBLNM,
				txtFILNM;
	JRadioButton	rdbMODTR,
					rdbADDTR;
	ButtonGroup	btgTRMOD;
	File file;
	RandomAccessFile file_io;
	ResultSetMetaData meta;
	Object[][] LM_COLDTL;
	Vector<String> vtr_COLNM;
	JList lst_COLNM;
	JScrollPane srp_COLNM;
	ResultSet LM_RSSET;
//	PreparedStatement	prp_INSST,prp_UPDST;
	cl_dattr()
	{
		super(1);
		txtTBLNM=new JTextField();
		txtFILNM=new JTextField();
		rdbMODTR=new JRadioButton("Modify");
		rdbADDTR=new JRadioButton("Overwrite");
		btgTRMOD=new ButtonGroup();
		btgTRMOD.add(rdbMODTR);
		btgTRMOD.add(rdbADDTR);
		lst_COLNM=new JList();
		lst_COLNM.setSelectionMode(2 );
		srp_COLNM=new JScrollPane(lst_COLNM);
		setMatrix(20,4);
		add(new JLabel("Enter AS Table Name : "),1,1,1,1,this,'L');
		add(txtTBLNM,1,2,1,1,this,'L');
		add(new JLabel("Enter Text File Name : "),1,3,1,1,this,'L');
		add(txtFILNM,1,4,1,1,this,'L');
		add(new JLabel("Select Transfer Mode : "),2,1,1,1,this,'L');
		add(rdbMODTR,2,2,1,1,this,'L');
		add(rdbADDTR,2,3,1,1,this,'L');
		add(new JLabel("Select Key Columns : "),3,1,1,1,this,'L');
		add(srp_COLNM,3,2,5,1,this,'L');
	}
	boolean vldDAT()
	{
		if(txtTBLNM.getText().equals(""))
		{
			setMSG("Enter Table Name .. ",'E');
			txtTBLNM.requestFocus();
			return false;
		}
		else if(txtFILNM.getText().equals(""))
		{
			setMSG("Enter File Name .. ",'E');
			txtFILNM.requestFocus();
			return false;
		}
		else if((!rdbMODTR.isSelected())&&(!rdbADDTR.isSelected()))
		{
			setMSG("Select Transfer Mode .. ",'E');
			return false;
		}
		return true;
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		Object L_SRC=L_AE.getSource();
		try{
		if(L_SRC==txtTBLNM)
		{
			M_strSQLQRY="select * from "+txtTBLNM.getText();
			LM_RSSET= cl_dat.exeSQLQRY1(M_strSQLQRY);
			meta=LM_RSSET.getMetaData();
			ResultSet L_RSSET=cl_dat.M_conSPDBA_pbst.getMetaData().getPrimaryKeys(null,null,txtTBLNM.getText());			
			ResultSetMetaData m=L_RSSET.getMetaData();
			System.out.println(meta.getColumnCount());
			LM_COLDTL=new Object[meta.getColumnCount()][3];
			System.out.println(LM_COLDTL);
			vtr_COLNM=new Vector<String>(5,2);
			while(L_RSSET.next())
			{
				vtr_COLNM.addElement(L_RSSET.getString(4));
			}
			L_RSSET=null;
			for(int i=1;i<meta.getColumnCount()+1;i++)
			{
				LM_COLDTL[i-1][0]=meta.getColumnName(i);
				LM_COLDTL[i-1][1]=(Object)(new Integer(meta.getColumnDisplaySize(i)));
				LM_COLDTL[i-1][2]=meta.getColumnTypeName(i);
			}
		}
		}catch(Exception E)
		{System.out.println(E);}
	}
	void exeSAVE()
	{
		String L_STRSQL="";
		String L_STRTMP ="",L_COLVL;
		StringTokenizer L_STRTKN;
		int L_COLNO =0;
		System.out.println("exeSAVE");
		if(vldDAT())
		{
			
			StringBuffer st=new StringBuffer("");
			StringBuffer st1=new StringBuffer("");
			try
			{
				System.out.println("1");
        //    file=new File("C:\\splerp\\datatfr\\catmst.txt");
		//	file=new File("C:\\splerp\\datatfr\\samst.txt");
		//	file=new File("C:\\splerp\\datatfr\\catdes.txt");
		  //  file=new File("C:\\splerp\\datatfr\\ismst.txt");
		//	file=new File("C:\\splerp\\datatfr\\rep0203.txt");
			file=new File("D:\\splerp\\datatfr\\DTM.txt");
			file_io=new RandomAccessFile(file,"rw");
			}
			catch(Exception L_E)
			{
				System.out.println("X "+L_E);
			}
			//for(int j=0;j<32800;j++) catmst
			//for(int j=0;j<4490;j++) //grin
			//while(!file_io.eof())
			//for(int j=0;j<11740;j++) //issues
			int j =0,intSAVED =0;
			boolean L_FLAG = true;
	//		while(L_FLAG)
			
			for(j=0;j<5;j++) //issues
			{
				try{
				j++;
				cl_dat.M_flgLCUPD_pbst = true;
				setMSG(" j: "+j+"SAVED: "+intSAVED,'N');				
				
			//	L_STRSQL = "insert into MM_ISMST values (";
			//	L_STRSQL = "insert into MM_GRMST values (";
            //  L_STRSQL = "insert into CO_CTMST1 values (";
			//  L_STRSQL = "insert into MM_STMST values (";
			//	L_STRSQL = "insert into MM_SAMST values (";
				L_STRSQL = "insert into TT_DTM values (";
				//while(!file_io.eof())
				//{
					st=new StringBuffer(file_io.readLine());
					L_COLNO=0;//Col number
					if(st == null)
						L_FLAG = false;
					L_STRTMP=st.toString();
					if(L_STRTMP.length() == 0)
						L_FLAG = false;
					
					L_STRTKN=new StringTokenizer(L_STRTMP,"|");	
					int tkn =0;
					tkn = L_STRTKN.countTokens();
					System.out.println("Tokens "+tkn);
					System.out.println(LM_COLDTL.length);
					for(int i=0;i<=tkn-1;i++)
					{
					L_COLVL=L_STRTKN.nextToken().trim();
					if(LM_COLDTL[L_COLNO][2].toString().equals("VARCHAR"))
					{
						if(i==tkn-1)
						{
							L_STRSQL +="'"+L_COLVL +"')";
						}
						else
							L_STRSQL +="'"+L_COLVL +"',";
						L_COLNO++;
						
					}
					else if(LM_COLDTL[L_COLNO][2].toString().equals("CHAR"))
					{
						if(i==tkn-1)
						{
							L_STRSQL +="'"+L_COLVL +"')";
						}
						else
							L_STRSQL +="'"+L_COLVL +"',";
						L_COLNO++;
						
					}
					else if(LM_COLDTL[L_COLNO][2].toString().equals("DECIMAL"))
					{
						if(i==tkn-1)
							L_STRSQL +=nvlSTRVL(L_COLVL,"0)");
						else
							L_STRSQL +=nvlSTRVL(L_COLVL,"0")+",";
						L_COLNO++;
					}
					else if(LM_COLDTL[L_COLNO][2].toString().equals("DATE"))
					{
						if(i==tkn-1)
						{
							if(L_COLVL.length() == 0)
								L_STRSQL += " null)";
							else
							L_STRSQL +="'"+L_COLVL.trim() +"')";
						}
						else
						{
							if(L_COLVL.length() == 0)
								L_STRSQL += "null,";
								else
									L_STRSQL +="'"+L_COLVL.trim() +"',";

						}

						L_COLNO++;
					}
					else if(LM_COLDTL[L_COLNO][2].toString().equals("TIMESTAMP"))
					{
						if(i==tkn-1)
						{
							if(L_COLVL.length() == 0)
								L_STRSQL += " null)";
							else
							L_STRSQL +="'"+L_COLVL.trim() +"')";
						}
						else
						{
							if(L_COLVL.length() == 0)
								L_STRSQL += "null,";
								else
									L_STRSQL +="'"+L_COLVL.trim() +"',";

						}

						L_COLNO++;
					}
					}
					//if(L_COLNO==meta.getColumnCount())
					//	break;
					System.out.println(L_STRSQL);
					cl_dat.exeSQLUPD(L_STRSQL+")","");
				//	System.out.println(L_STRSQL);
				//	cl_dat.exeSQLUPD(L_STRSQL,"");
					if(cl_dat.exeDBCMT("save"))
					{
						intSAVED++;
					}
					else
					{
						System.out.println(L_STRSQL);
					}
						
				}
				catch(IOException E)
				{
					L_FLAG = false;
					System.out.println("in catch"+E+"  "+st.toString()+L_STRSQL);
				}
				catch(Exception E)
				{
					System.out.println("in catch"+E+L_STRSQL);
				}
				/*try
				{
					cl_dat.exeSQLUPD(L_STRSQL,"");
					cl_dat.exeDBCMT("save");
					//cl_dat.stmSPDBA.executeUpdate(st1.toString());
				}
				catch(Exception E)
				{
					System.out.println("in catch"+E+"  "+st1.toString());
				}*/
		
			}
			
		}
		
	}
}
// insert into spltest/sr_rpmst values ('51300001','01',1  , '6711020051','12        ','12                            ','12                  ',8.000000  , '0',' ','06/13/2003','12                                           ','SYS','N')

//UPDATE spltest/sr_lcmst SET (LC_LOCID,LC_LOCDS,LC_CPUOM,LC_LCSTK,LC_LCCAP,LC_STSFL,LC_TRNFL,LC_LUSBY,LC_LUPDT)=('A1   ','NORTH               ','No ',0.000   , 2000.000, ' ','0','SYS',''         ')  where  LC_LOCID = 'A1   '
