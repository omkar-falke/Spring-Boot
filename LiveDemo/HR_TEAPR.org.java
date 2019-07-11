import javax.swing.*;
import java.io.IOException;import java.io.File;import java.io.RandomAccessFile;
import java.util.Vector;import java.util.StringTokenizer;
import java.sql.ResultSet;import java.sql.ResultSetMetaData;
import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.Color;import java.awt.Container;import java.awt.Toolkit;
import java.lang.Thread;import java.awt.Cursor;import java.awt.Component;
import java.sql.*;
import java.sql.CallableStatement;
class hr_teapr extends JFrame implements ActionListener,Runnable
{
   	private File file;
	private RandomAccessFile file_io;
	private JButton btnPROCS,btnSTOP,btnEXIT;
	private JLabel lblMESG;
	private Thread thrAPR;
	private boolean FLAG = true;
	private CallableStatement cstEMPCT;
	private cl_pbase ocl_pbase;
	private Connection conSPDBA;
	private Statement stmSPDBA;
    hr_teapr()
    {
        super("Head count updating");
        try
        {
            cl_dat.M_dimSCRN_pbst = Toolkit.getDefaultToolkit().getScreenSize();
			cl_dat.M_dblWIDTH=(cl_dat.M_dimSCRN_pbst.width/800.00);
			cl_dat.M_dblHIGHT=((cl_dat.M_dimSCRN_pbst.height)/600.00);
            ocl_pbase = new cl_pbase();
            ocl_pbase.setMatrix(20,8);
            ocl_pbase.add(btnPROCS = new JButton("Process"),2,1,1,1,ocl_pbase,'L');
            ocl_pbase.add(btnSTOP = new JButton("STOP"),2,2,1,1,ocl_pbase,'L');
            ocl_pbase.add(btnEXIT = new JButton("EXIT"),2,3,1,1,ocl_pbase,'L');
            
            ocl_pbase.add(lblMESG = new JLabel(" "),4,1,1,8,ocl_pbase,'L');
            Container L_ctrTHIS=getContentPane();
			L_ctrTHIS.add(ocl_pbase);
		    //setSize(cl_dat.M_dimSCRN_pbst.width,cl_dat.M_dimSCRN_pbst.height);
		    setSize(800,200);
			show();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			Component L_cmpTEMP=null;
			//Unregister listners of cl_pbase and register local listeners
			for(int i=0;i<ocl_pbase.M_vtrSCCOMP.size();i++)
			{
				L_cmpTEMP=(Component)ocl_pbase.M_vtrSCCOMP.elementAt(i);
				L_cmpTEMP.removeKeyListener(ocl_pbase);
				if(L_cmpTEMP instanceof JTextField)
				{
					((JTextField)L_cmpTEMP).removeActionListener(ocl_pbase);
					((JTextField)L_cmpTEMP).removeFocusListener(ocl_pbase);
				}
				else if(L_cmpTEMP instanceof AbstractButton)
				{
					((AbstractButton)L_cmpTEMP).removeActionListener(ocl_pbase);
					((AbstractButton)L_cmpTEMP).removeFocusListener(ocl_pbase);
				}
				else if(L_cmpTEMP instanceof JComboBox)
				{
					((JComboBox)L_cmpTEMP).removeActionListener(ocl_pbase);
					L_cmpTEMP.removeFocusListener(ocl_pbase);
				}
			}
			btnPROCS.addActionListener(this);btnSTOP.addActionListener(this);btnEXIT.addActionListener(this);
			L_cmpTEMP=null;
            
            thrAPR = new Thread(this);
            thrAPR.start();
            setMSG("Press Stop to pause the thread",'N');
      	}
	    catch(Exception E)
		{
            System.out.println(E);
        }

    }
    public void run()
    {
        while(FLAG)
        {
            //System.out.println("Thread Started");
            process();
        }
    }
public void actionPerformed(ActionEvent L_AE)
{
    if(L_AE.getSource().equals(btnPROCS))
	{
	    process();
	}
	if(L_AE.getSource().equals(btnSTOP))
	{
	    FLAG = false;
	}
	if(L_AE.getSource().equals(btnEXIT))
	{
	    System.exit(0);
	}
}
void process()
{
    String L_STRSQL="";
    String L_STRTMP ="",L_COLVL;
    StringTokenizer L_STRTKN;
    String L_STRTIM ="";
    int L_COLNO =0;
    StringBuffer st=new StringBuffer("");
    try
    {
    	conSPDBA=setCONDTB("01","SPLDATA","FIMS","FIMS");
        if(conSPDBA != null)
        {
            setMSG("Connected to Database..",'N');
        }
        if(conSPDBA == null)
        {
            setMSG("Failed to Connect..",'E');
            return; 
        }
        stmSPDBA = conSPDBA.createStatement();
        cstEMPCT = conSPDBA.prepareCall("{call hr_ephct()}");
   // 	file=new File("c:\\reports\\ts.txt");
    	file=new File("M:\\TimeStampForPayroll\\ts.txt");
        file_io=new RandomAccessFile(file,"r");
    }
    catch(Exception L_E)
    {
    	System.out.println("File Not Found");
    	setMSG("File Not Found ..will retry after 5 minutes",'N');
    	try
    	{
    	    thrAPR.sleep(300000);
    	    return ;
    	}
    	catch(Exception L_EX)
    	{
    	   System.out.println("Error"); 
    	}
    }
    int j =0,intSAVED =0,intERR =0;
    boolean L_FLAG = true;
    String st2 ="";
    for(j=0;j<2000;j++) //issues
    //while(!file_io.eof())
    while(L_FLAG)
    {
    	try
    	{
    		j++;
    		cl_dat.M_flgLCUPD_pbst = true;
    		setMSG(" j: "+j+" SAVED: "+intSAVED,'N');				
    		
    		//L_STRSQL = "insert into HR_EPALG(EPA_EMPNO,EPA_PNCTM,EPA_INOCD,EPA_TRMID,EPA_SPRSN,EPA_STSFL) values (";
    		L_STRSQL = "insert into HR_EPALG(EPA_EMPNO,EPA_PNCTM,EPA_INOCD,EPA_TRMID,EPA_STSFL) values (";
    		st=new StringBuffer(file_io.readLine());
    		L_COLNO=0;//Col number
    		if(st == null)
    			L_FLAG = false;
    		L_STRTMP=st.toString();
    		if(L_STRTMP.length() == 0)
    			L_FLAG = false;
    		
    		L_STRTKN=new StringTokenizer(L_STRTMP,"\t");	
    		int tkn =0;
    		tkn = L_STRTKN.countTokens();
    		for(int i=0;i<=tkn-1;i++)
    		{
    			L_COLVL=L_STRTKN.nextToken().trim();
    			if(i==0)
    		    	L_STRSQL +="'"+L_COLVL +"',";
    		    else if(i==1)
    		    {
        		    if(L_COLVL.length() == 0)
       					L_STRSQL += "null,";
    				else
    				{
    				//	L_STRTIM = "20"+L_COLVL.substring(6,8)+"-"+L_COLVL.substring(3,5)+"-"+L_COLVL.substring(0,2)+"-"+L_COLVL.substring(9,11)+"."+L_COLVL.substring(12,14)+"."+L_COLVL.substring(15,17);
                        L_STRTIM = L_COLVL.trim() ;
                        L_STRSQL +="'"+L_STRTIM +"',";
    				}
    		    }
    		    else if((i==2)||(i==3)||(i==4))
    		    	L_STRSQL +="'"+L_COLVL +"',";
    		   
    		}
    		L_STRSQL +="'0')";
    		/*cl_dat.exeSQLUPD(L_STRSQL,"");
    		if(cl_dat.exeDBCMT("save"))
    		{
    			intSAVED++;
    		}
    		else
    		{
    			System.out.println(L_STRSQL);
    			intERR++;
    		}*/
    		if(stmSPDBA.executeUpdate(L_STRSQL)>0)
    		{
    		    conSPDBA.commit();
    		    intSAVED++;
    		}
    		else
    		{
    		    conSPDBA.rollback();
    		    intERR++;   
    		}
    		
    	}
    	catch(IOException E)
    	{
    		L_FLAG = false;
    		System.out.println("in catch"+E+"  "+st.toString()+L_STRSQL);
    	}
    	catch(NullPointerException L_NE)
    	{
    		L_FLAG = false;
    	}
    	catch(Exception E)
    	{
    		System.out.println("in catch"+E+L_STRSQL);
    	}
    }
    System.out.println("Saved "+intSAVED+ " Not Saved "+intERR);
    try
    {
        file_io.close();
        System.out.println("call to stored procedure");        
        cstEMPCT.executeUpdate();
        if(conSPDBA !=null)
            conSPDBA.commit();
    // Deletion Commented Temporarily
    //    file=new File("c:\\reports\\ts.txt");
        file=new File("M:\\TimeStampForPayroll\\ts.txt");
        file.delete();
        if(conSPDBA !=null)
            conSPDBA.close();
        thrAPR.sleep(300000);
        System.out.println("Sleep");
    }
    catch(Exception L_E)
    {
        setMSG(L_E.toString(),'E');
        System.out.println(L_E.toString());
    }
     finally
     {
        stmSPDBA = null;
        conSPDBA = null;
        cstEMPCT = null;
     }
    }
    public static void main(String args[])
    {
        hr_teapr ohr_teapr = new hr_teapr();
        //ohr_teapr.show();
    }
    private  Connection setCONDTB(String LP_SYSLC,String LP_DTBLB, String LP_DTBUS, String LP_DTBPW)
    {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		Connection LM_CONDTB=null;
		try
		{
			String L_STRURL = "", L_STRDRV = "";
			if(LP_SYSLC.equals("01"))
			{
				L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
				L_STRURL = "jdbc:as400://SPLWS01/";
				Class.forName(L_STRDRV);
			}
			else if(LP_SYSLC.equals("02"))
			{
				L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
				L_STRURL = "jdbc:as400://SPLHOS01/";
				Class.forName(L_STRDRV);
/*				int port = 50000;
				LP_DTBUS = "SPLDATA";
				LP_DTBPW = "SPLDATA";

				L_STRURL = "jdbc:db2://" + "splhos01" + ":" + 50000 + "/" ;
				Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
*/			}
			else if(LP_SYSLC.equals("03"))
			{
				L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
				L_STRURL = "jdbc:as400://MUMAS2/";
				Class.forName(L_STRDRV);
			}
			L_STRURL = L_STRURL + LP_DTBLB;
			LM_CONDTB = DriverManager.getConnection(L_STRURL,LP_DTBUS,LP_DTBPW);

			if(LM_CONDTB == null)
				return null;
			LM_CONDTB.setAutoCommit(false);

			SQLWarning L_STRWRN = LM_CONDTB.getWarnings();
			return LM_CONDTB;
		}
		catch(java.lang.Exception L_EX)
		{
			setMSG("Error while connecting to DB : "+L_EX,'E');
			return null;
		}
		finally{this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));}
	}

     private void setMSG(String P_strMESG,char P_chrMSGTP)
	{
		try
		{
			if(P_chrMSGTP == 'N')
			{
				lblMESG.setForeground(Color.blue);
				lblMESG.setText(P_strMESG);
			}
			else
			{
				lblMESG.setForeground(Color.red);
				lblMESG.setText(P_strMESG);
			}
			P_strMESG=null;
			System.gc();
		}
		catch (Exception e)
		{
			setMSG(e.toString(),'E');
		}
	}

}

 