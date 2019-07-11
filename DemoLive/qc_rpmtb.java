import javax.swing.JTextField;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;import javax.swing.JLabel;import javax.swing.JComboBox;import javax.swing.JComponent;import javax.swing.InputVerifier;
import java.awt.event.*;
import java.sql.SQLException;import java.sql.DriverManager;import java.sql.Connection;import java.sql.Statement;import java.sql.ResultSet;
import java.io.File;import java.io.FileOutputStream;import java.io.DataOutputStream;

public class qc_rpmtb extends cl_rbase
{
   private String[] staQPRCD;
   private String strLOTNO,strCLSFL,strPRDCD,strTSTTP,strLOTDT,strLINNO;
   private String strTODAT,strFRDAT,strRUNNO,strPRDDS,strPSTDT;
   private FileOutputStream fosREPORT;  /** File Output Stream for File Handling */
   private DataOutputStream dosREPORT;  /** Data Output Stream for generating Report File */
   private String strFILNM;             /** String for generated Report File Name*/
   private int intRECCT = 0;            /** Integer for counting the records fetched from DB.*/
   private String[] staHLPHD = new String[2]; // For Help Header 
   private JTextField txtFRDAT,txtTODAT,txtPRDCD,txtPRDDS,txtLINNO;
   private ButtonGroup bgrPRDCD;
   private JRadioButton rdbTPRCD,rdbPRDCD;	   
   private ButtonGroup bgrTSTTP;
   private JRadioButton rdbTSCOM,rdbTSGRB;
   private ButtonGroup bgrRPTTP;
   private JRadioButton rdbDBFRP,rdbXLSRP;	   
   private ResultSet rstRSSET;
   private String strOPTVL = "T";
   private String strTSGRB = "0101";
   private String strTSCOM = "0103";
   private boolean flgVLDVL = false;   
   private String strQCATP,strPRDTP;
   private Connection conSPBKA;
   private Statement stmSTBK1;
   private JComboBox cmbPRDTP;
   private String strDSPVL,strMFIVL,LM_IZOVL,LM_VICVL,LM_A__VL,LM_TS_VL,LM_EL_VL,LM_B__VL,LM_Y1_VL,LM_WI_VL,LM_RSMVL;
   private InputVerifier INPVF;
  	qc_rpmtb()
	{
		super(2);
		try
		{
    		setMatrix(20,8);
    	    M_vtrSCCOMP.remove(M_lblFMDAT);
    		M_vtrSCCOMP.remove(M_lblTODAT);
    		M_vtrSCCOMP.remove(M_txtTODAT);
    		M_vtrSCCOMP.remove(M_txtFMDAT);
    		bgrRPTTP = new ButtonGroup();
    		bgrPRDCD = new ButtonGroup();
    		bgrTSTTP = new ButtonGroup();
    		
    		add(new JLabel("Report type "),4,3,1,1,this,'L');
            add(rdbDBFRP= new JRadioButton("DBF Format",true),4,4,1,1.5,this,'L');									
    		add(rdbXLSRP= new JRadioButton("Excel Format",false),4,6,1,1.5,this,'R');
    		bgrRPTTP.add(rdbDBFRP);
    		bgrRPTTP.add(rdbXLSRP);
    		add(new JLabel("Grade "),5,3,1,1,this,'L');
    		add(rdbTPRCD= new JRadioButton("Target Grade",true),5,4,1,1.5,this,'L');									
    		add(rdbPRDCD= new JRadioButton("Class. Grade",false),5,6,1,1.5,this,'R');									
    		bgrPRDCD.add(rdbTPRCD);
    		bgrPRDCD.add(rdbPRDCD);
    		add(new JLabel("Test Type"),6,3,1,1,this,'L');
    		add(rdbTSCOM= new JRadioButton("Composite",true),6,4,1,1.5,this,'L');									
    		add(rdbTSGRB= new JRadioButton("Grab Test",false),6,6,1,1.5,this,'R');	
    		bgrTSTTP.add(rdbTSCOM);
    		bgrTSTTP.add(rdbTSGRB);								
    		add(new JLabel("Product Code"),7,3,1,1,this,'L');
    		add(txtPRDCD = new TxtLimit(10),7,4,1,1,this,'L');									
    		add(txtPRDDS = new TxtLimit(30),7,5,1,2,this,'L');									
    		add(new JLabel("Date Range"),8,3,1,1,this,'L');
    		add(txtFRDAT = new TxtDate(),8,4,1,1,this,'L');									
    		add(txtTODAT = new TxtDate(),8,5,1,1,this,'L');									
    		add(new JLabel("Line Number"),9,3,1,1,this,'L');
    		add(txtLINNO = new TxtLimit(2),9,4,1,1,this,'L');									
    		strTSTTP = strTSCOM;	
    		INPVF objINPVF = new INPVF();
    		txtTODAT.setInputVerifier(objINPVF);
    		txtFRDAT.setInputVerifier(objINPVF);
    		txtLINNO.setInputVerifier(objINPVF);
    		txtPRDCD.setInputVerifier(objINPVF);
    		cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
    		cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
    		setENBL(false);
    		add(new JLabel("Product Type"),3,3,1,1,this,'L');
    		add(cmbPRDTP = new JComboBox(),3,4,1,2,this,'L');
    		M_strSQLQRY ="Select CMT_CODCD,CMT_CODDS from CO_CDTRN WHERE isnull(CMT_STSFL,'') <>'X'"+
    		            " AND CMT_CGMTP ='MST' AND CMT_CGSTP ='COXXPRD'";
    		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    		if(M_rstRSSET !=null)
    		    while(M_rstRSSET.next())
    		    {
    	            cmbPRDTP.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));      	        
    		    }
		}
		catch(Exception L_E)
		{
            setMSG(L_E,"constructor");		    
		}
	}
	void setENBL(boolean L_flgSTAT)
	{
        super.setENBL(L_flgSTAT);
        txtPRDDS.setEnabled(false);
        
	}
private void getLOTRNG()
{
	try
	{
	    ResultSet L_rstRSSET ;
    	String L_strQPRVL,L_strINSQR;
    	java.util.Date datTEMP;
    	strPRDCD = txtPRDCD.getText().trim();
    	strFRDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText().trim()));
    	strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
    	strLINNO = txtLINNO.getText().trim();
    	M_strSQLQRY =  " SELECT LT_LOTNO,LT_RUNNO,CONVERT(varchar,LT_PSTDT,103) L_DATE,LT_PRDCD,PS_TSTDT, ";
    	M_strSQLQRY += "PS_DSPVL,PS_MFIVL,PS_IZOVL,PS_VICVL,PS_TS_VL,PS_EL_VL,PS_RSMVL,PS_WI_VL,PS_A__VL,PS_B__VL,PS_Y1_VL ";
    	M_strSQLQRY += " from PR_LTMST,QC_PSMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = PS_LOTNO AND LT_RCLNO = PS_RCLNO AND";
    	M_strSQLQRY += " PS_TSTTP ='" + strTSTTP +"' and ";
    	if(rdbTPRCD.isSelected())
    	{
    		M_strSQLQRY += " LT_TPRCD = ";
    		M_strSQLQRY += "'"+ strPRDCD+"'";
    	}
    	else if(rdbPRDCD.isSelected())
    	{
    		M_strSQLQRY += " LT_CLSFL = '9' and LT_PRDCD = ";
    		M_strSQLQRY += "'"+ strPRDCD+"'";
    	}
    	M_strSQLQRY += " AND LT_RCLNO ='00'";
    	M_strSQLQRY += " AND LT_LINNO ='"+strLINNO+"'";
    	M_strSQLQRY += " AND CONVERT(varchar,LT_PSTDT,103) BETWEEN '" + strFRDAT.trim() + "' AND '" + strTODAT.trim() +"'";
    	//M_strSQLQRY += " AND LT_CLSFL = '9'";
    	//M_strSQLQRY += " AND LT_CLSFL <> '8'";
    	M_strSQLQRY += " ORDER BY LT_RUNNO,LT_PRDCD,LT_LOTNO,PS_TSTDT";
    	int i=0,j=0,L_intROWCT = 0,L_intCOUNT =0;
    	M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
    	if(M_rstRSSET !=null)
    	    while(M_rstRSSET.next())
		    {
				L_strINSQR = "Insert into dt_mtbdt(runno,lotno,grade,date,dsp,mfi,";
				L_strINSQR +="izo,vic,ts,el,rsm,wi,a,b,y1)values(";
				strLOTNO = M_rstRSSET.getString("LT_LOTNO");
				strRUNNO = M_rstRSSET.getString("LT_RUNNO");
				datTEMP = M_rstRSSET.getDate("L_DATE");
				if(datTEMP !=null)
				    strPSTDT = M_fmtLCDAT.format(datTEMP);
				 strPSTDT =  M_fmtDBDAT.format(M_fmtLCDAT.parse(strPSTDT));  
				 getPRDDS(strPRDCD);
				 strDSPVL = M_rstRSSET.getString("PS_DSPVL");				
				 strMFIVL = M_rstRSSET.getString("PS_MFIVL");				
				 LM_IZOVL = M_rstRSSET.getString("PS_IZOVL");				
				 LM_VICVL = M_rstRSSET.getString("PS_VICVL");				
				 LM_TS_VL = M_rstRSSET.getString("PS_TS_VL");				
				 LM_EL_VL = M_rstRSSET.getString("PS_EL_VL");				
				 LM_RSMVL = M_rstRSSET.getString("PS_RSMVL");				
				 LM_WI_VL = M_rstRSSET.getString("PS_WI_VL");
				 LM_A__VL = M_rstRSSET.getString("PS_A__VL");				
				 LM_B__VL = M_rstRSSET.getString("PS_B__VL");				
				 LM_Y1_VL = M_rstRSSET.getString("PS_Y1_VL");				
		         L_strINSQR += "'" + nvlSTRVL(strRUNNO,"") + "',";
				 L_strINSQR += "'" + nvlSTRVL(strLOTNO,"") + "',";
				 L_strINSQR += "'" + nvlSTRVL(strPRDDS,"") + "',";
				 if(nvlSTRVL(strPSTDT.trim(),"").length()>0)
					L_strINSQR += "ctod('"+strPSTDT+ "'),";
				 else
					 L_strINSQR += "ctod('  /  /    '),";
				 L_strINSQR += nvlSTRVL(strDSPVL,"0") + ",";
				 L_strINSQR += nvlSTRVL(strMFIVL,"0") + ",";
				 L_strINSQR += nvlSTRVL(LM_IZOVL,"0") + ",";
				 L_strINSQR += nvlSTRVL(LM_VICVL,"0") + ",";
				 L_strINSQR += nvlSTRVL(LM_TS_VL,"0") + ",";
				 L_strINSQR += nvlSTRVL(LM_EL_VL,"0") + ",";
				 L_strINSQR += nvlSTRVL(LM_RSMVL,"0") + ",";
				 L_strINSQR += nvlSTRVL(LM_WI_VL,"0") + ",";
				 L_strINSQR += nvlSTRVL(LM_A__VL,"0") + ",";
				 L_strINSQR += nvlSTRVL(LM_B__VL,"0") + ",";
				 L_strINSQR += nvlSTRVL(LM_Y1_VL,"0")+")";
				 cl_dat.M_flgLCUPD_pbst = true;
				L_intCOUNT++;
				if(stmSTBK1.executeUpdate(L_strINSQR) >0)
				{
					i++;
				}
				else
				{
					j++;
				}
				setMSG("saved "+i+" not saved "+j,'N');
				conSPBKA.commit();
			}
    		if(L_intCOUNT >0)
    		{
    		    conSPBKA.commit();
    			setMSG("Data Transfer File has been created at c:\\reports\\qc_rpmtb.dbf ",'N');
    		}
    		else
    			setMSG("No Data Found for the given input data.. ",'E');
    	}
    	catch(SQLException L_SE)
    	{
    		setMSG(L_SE,"getLOTRNG");
    	}
    	catch(Exception L_SE)
    	{
    		setMSG(L_SE,"getLOTRNG");
    	}
}
private void getLOTRNG1()
{
	try
	{
	    fosREPORT = new FileOutputStream(strFILNM);
		dosREPORT = new DataOutputStream(fosREPORT);
		intRECCT  = 0 ;
		ResultSet L_rstRSSET ;
    	String L_strQPRVL,L_strINSQR;
    	java.util.Date datTEMP;
    	strPRDCD = txtPRDCD.getText().trim();
    	strFRDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFRDAT.getText().trim()));
    	strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
    	strLINNO = txtLINNO.getText().trim();
    	prnHEADER();
    	M_strSQLQRY =  " SELECT LT_LOTNO,LT_RUNNO,CONVERT(varchar,LT_PSTDT,103) L_DATE,LT_PRDCD,PS_TSTDT, ";
    	M_strSQLQRY += "PS_DSPVL,PS_MFIVL,PS_IZOVL,PS_VICVL,PS_TS_VL,PS_EL_VL,PS_RSMVL,PS_WI_VL,PS_A__VL,PS_B__VL,PS_Y1_VL ";
    	M_strSQLQRY += " from PR_LTMST,QC_PSMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = PS_LOTNO AND LT_RCLNO = PS_RCLNO AND";
    	M_strSQLQRY += " PS_TSTTP ='" + strTSTTP +"' and ";
    	if(rdbTPRCD.isSelected())
    	{
    		M_strSQLQRY += " LT_TPRCD = ";
    		M_strSQLQRY += "'"+ strPRDCD+"'";
    	}
    	else if(rdbPRDCD.isSelected())
    	{
    		M_strSQLQRY += " LT_CLSFL = '9' and LT_PRDCD = ";
    		M_strSQLQRY += "'"+ strPRDCD+"'";
    	}
    	M_strSQLQRY += " AND LT_RCLNO ='00'";
    	M_strSQLQRY += " AND LT_LINNO ='"+strLINNO+"'";
    	M_strSQLQRY += " AND CONVERT(varchar,LT_PSTDT,103) BETWEEN '" + strFRDAT.trim() + "' AND '" + strTODAT.trim() +"'";
    	//M_strSQLQRY += " AND LT_CLSFL = '9'";
    	//M_strSQLQRY += " AND LT_CLSFL <> '8'";
    	M_strSQLQRY += " ORDER BY LT_RUNNO,LT_PRDCD,LT_LOTNO,PS_TSTDT";
    	int i=0,j=0,L_intROWCT = 0,L_intCOUNT =0;
    	M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
    	//System.out.println(M_strSQLQRY);
    	if(M_rstRSSET !=null)
    	{
    	    while(M_rstRSSET.next())
		    {
				strLOTNO = M_rstRSSET.getString("LT_LOTNO");
				//System.out.println("LOTNO = " + strLOTNO);
				strRUNNO = M_rstRSSET.getString("LT_RUNNO");
				datTEMP = M_rstRSSET.getDate("L_DATE");
				if(datTEMP !=null)
				    strPSTDT = M_fmtLCDAT.format(datTEMP);
				 //strPSTDT =  M_fmtDBDAT.format(M_fmtLCDAT.parse(strPSTDT));  
				 getPRDDS(strPRDCD);
				 strDSPVL = M_rstRSSET.getString("PS_DSPVL");
				 //System.out.println("A = " + strDSPVL);
				 strMFIVL = M_rstRSSET.getString("PS_MFIVL");
				 //System.out.println("B = " + strMFIVL);
				 LM_IZOVL = nvlSTRVL(M_rstRSSET.getString("PS_IZOVL"),"0");
				 //System.out.println("C = " + LM_IZOVL);
				 LM_VICVL = M_rstRSSET.getString("PS_VICVL");
				 //System.out.println("D = " + LM_VICVL);
				 LM_TS_VL = M_rstRSSET.getString("PS_TS_VL");
				 //System.out.println("E = " + LM_TS_VL);
				 LM_EL_VL = M_rstRSSET.getString("PS_EL_VL");
				 //System.out.println("F = " + LM_EL_VL);
				 LM_RSMVL = M_rstRSSET.getString("PS_RSMVL");
				 //System.out.println("G = " + LM_RSMVL );
				 LM_WI_VL = M_rstRSSET.getString("PS_WI_VL");
				 //System.out.println("H = " + LM_WI_VL);
				 LM_A__VL = setNumberFormat(M_rstRSSET.getDouble("PS_A__VL"),3);
				 //System.out.println("I = " + LM_A__VL);
				 LM_B__VL = setNumberFormat(M_rstRSSET.getDouble("PS_B__VL"),3);
				 //System.out.println("J = " + LM_B__VL );
				 LM_Y1_VL = setNumberFormat(M_rstRSSET.getDouble("PS_Y1_VL"),3);
				 //System.out.println("K = " + LM_Y1_VL);
				 		         
		         /*dosREPORT.writeBytes(padSTRING('R',"",2)+padSTRING('R',strRUNNO,10)+padSTRING('R',strLOTNO,10)+padSTRING('R',strPRDDS,10)+padSTRING('L',strPSTDT,10)+padSTRING('L',strDSPVL,7));
		         dosREPORT.writeBytes(padSTRING('L',strMFIVL,7)+padSTRING('L',LM_IZOVL,7)+padSTRING('L',LM_VICVL,8)+padSTRING('L',LM_TS_VL,7)+padSTRING('L',LM_EL_VL,8));
		         dosREPORT.writeBytes(padSTRING('L',LM_RSMVL,7)+padSTRING('L',LM_WI_VL,8)+padSTRING('L',LM_A__VL,6)+padSTRING('L',LM_B__VL,6)+padSTRING('L',LM_Y1_VL,8)+"\n");*/
				 
				 dosREPORT.writeBytes(padSTRING('R',strRUNNO,10)+"\t"+padSTRING('R',strLOTNO,10)+"\t"+padSTRING('R',strPRDDS,10)+"\t"+padSTRING('L',strPSTDT,10)+"\t"+padSTRING('L',strDSPVL,7)+"\t");
		         dosREPORT.writeBytes(padSTRING('L',strMFIVL,7)+"\t"+padSTRING('L',LM_IZOVL,7)+"\t"+padSTRING('L',LM_VICVL,8)+"\t"+padSTRING('L',LM_TS_VL,7)+"\t"+padSTRING('L',LM_EL_VL,8)+"\t");
		         dosREPORT.writeBytes(padSTRING('L',LM_RSMVL,7)+"\t"+padSTRING('L',LM_WI_VL,8)+"\t"+padSTRING('L',LM_A__VL,6)+"\t"+padSTRING('L',LM_B__VL,6)+"\t"+padSTRING('L',LM_Y1_VL,8)+"\n");
			}
    		intRECCT = 1;
    		setMSG("Data Transfer File has been created at c:\\reports\\qc_rpmtb.xls ",'N');
    	}
    	M_rstRSSET.close();
    	dosREPORT.close();
		fosREPORT.close();
    }
    catch(SQLException L_SE)
    {
    	setMSG(L_SE,"getLOTRNG1");
    }
    catch(Exception L_SE)
    {
    	setMSG(L_SE,"getLOTRNG1");
    }
}

private void prnHEADER()                               // gets the header of the report
	{
		try
		{
		    if(rdbXLSRP.isSelected())
		    {
    			/*cl_dat.M_PAGENO += 1;
    			cl_dat.M_intLINNO_pbst = 0;
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			dosREPORT.writeBytes(padSTRING('R',"",4)+padSTRING('R',"RUNNO",10)+padSTRING('R',"LOTNO",8)+padSTRING('R',"GRADE",13)+padSTRING('R',"DATE",11)+padSTRING('R',"DSP",7));
    			dosREPORT.writeBytes(padSTRING('R',"MFI",7)+padSTRING('R',"IZO",7)+padSTRING('R',"VIC",8)+padSTRING('R',"TS",8)+padSTRING('R',"EL",7));
    			dosREPORT.writeBytes(padSTRING('R',"RSM",8)+padSTRING('R',"WI",6)+padSTRING('R',"A",6)+padSTRING('R',"B",7)+padSTRING('R',"Y1",8)+"\n");
    			cl_dat.M_intLINNO_pbst += 1;*/
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes(padSTRING('C',"RUNNO",10)+"\t"+padSTRING('C',"LOTNO",8)+"\t"+padSTRING('R',"GRADE",13)+"\t"+padSTRING('C',"DATE",11)+"\t"+padSTRING('C',"DSP",7)+"\t");
    			dosREPORT.writeBytes(padSTRING('C',"MFI",7)+"\t"+padSTRING('C',"IZO",7)+"\t"+padSTRING('C',"VIC",8)+"\t"+padSTRING('C',"TS",8)+"\t"+padSTRING('C',"EL",7)+"\t");
    			dosREPORT.writeBytes(padSTRING('C',"RSM",8)+"\t"+padSTRING('C',"WI",6)+"\t"+padSTRING('C',"A",6)+"\t"+padSTRING('C',"B",7)+"\t"+padSTRING('C',"Y1",8)+"\n");
    			dosREPORT.writeBytes("\n");
    		}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");	
		}
	} 
	
public void getPRDDS(String P_strPRDCD)
{
			
	strPRDDS = "";
	M_strSQLQRY = " Select PR_PRDDS from CO_PRMST where PR_PRDCD = ";
	M_strSQLQRY += "'"+ P_strPRDCD + "'";
	try
	{
		rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		if(rstRSSET!=null)
		{
	    	if(rstRSSET.next())
		    	strPRDDS = rstRSSET.getString("PR_PRDDS");
		    rstRSSET.close();
		}
		txtPRDDS.setText(strPRDDS);
	}
	catch(SQLException L_SE)
	{
		setMSG(L_SE,"getPRDDS");
	}
}
public boolean vldPRDCD(String P_strPRDCD)
{
	int L_intCOUNT =0;		
	strPRDDS = "";
	M_strSQLQRY = " Select PR_PRDDS from CO_PRMST where PR_PRDCD = ";
	M_strSQLQRY += "'"+ P_strPRDCD + "'";
	try
	{
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET.next())
		{
			strPRDDS = M_rstRSSET.getString("PR_PRDDS");
			txtPRDDS.setText(strPRDDS);
			return true;
		}
		else
		{
			setMSG("Product Code does not exist.. ",'N');
			return false;
		}
	}
	catch(SQLException L_SE)
	{
		setMSG(L_SE,"vldPRDCD");
	}
	return false;
}

private void crtLINE(int intCOUNT,String LP_SEPRTR)    //method to print lines in the report
	{
		String strln = "";
		try
		{
			for(int i=1;i<=intCOUNT;i++)
			{
		    	strln += LP_SEPRTR;
			}
			dosREPORT.writeBytes(strln);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtLINE");	
		}
	}
	
public void actionPerformed(ActionEvent L_AE)
{
     super.actionPerformed(L_AE);
     if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
     {
       if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
        if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() !=0)
            setENBL(true);
        else
        {
            setENBL(false);
            setMSG("Select an option ",'N');
        }
        
     }
     if(M_objSOURC == cmbPRDTP)
     {
        if(cmbPRDTP.getItemCount() > 0)
        {
            txtPRDCD.setText("");
            txtPRDDS.setText("");
            txtFRDAT.setText("");
            txtTODAT.setText("");
            txtLINNO.setText("");
        }
     }
       if (M_objSOURC == rdbPRDCD)
	   {
		  if(strTSTTP.equals(strTSGRB))
			{
				setMSG("Grab Test can not be taken for Classified Grade..",'E');
				rdbTSCOM.setSelected(true);
				strTSTTP = strTSCOM;
			}
			else
			{
				setMSG("",'N');
				
			}
	   }
	   else if(M_objSOURC == rdbTSGRB)
	   {
		 	if(rdbPRDCD.isSelected())
			{
				setMSG("Grab Test can not be taken for Classified Grade..",'E');
				rdbTSCOM.setSelected(true);
				strTSTTP = strTSCOM;
			}
			else
			{
				setMSG("",'N');
				strTSTTP = strTSGRB;
			}
	   }
	   else if (M_objSOURC == rdbTSCOM)
	   {
		   strTSTTP = strTSCOM;
	   }
}
public void keyPressed(KeyEvent L_KE)
{
     super.keyPressed(L_KE);
     if (L_KE.getKeyCode()== L_KE.VK_ENTER)
     {
        if(M_objSOURC == rdbTPRCD)
        {
            rdbPRDCD.requestFocus();
            setMSG("Press Space bar to select this option ",'N');
        }
        else if(M_objSOURC == rdbPRDCD)
        {
            rdbTSCOM.requestFocus();
            setMSG("Press Space bar to select this option ",'N');
        }
       else if(M_objSOURC == rdbTSCOM)
        {
            rdbTSGRB.requestFocus();
            setMSG("Press Space bar to select this option ",'N');
        }
        else if(M_objSOURC == rdbTSGRB)
        {
            txtPRDCD.requestFocus();
            setMSG("Enter the Product Code.. ",'N');
        }
        else if(M_objSOURC == txtPRDCD)
        {
            txtFRDAT.requestFocus();
            setMSG("Enter the value of To Date.. ",'N');
        }
        else if(M_objSOURC == txtFRDAT)
        {
            txtTODAT.requestFocus();
            setMSG("Enter the value of To Date.. ",'N');
        }
        else if(M_objSOURC == txtTODAT)
        {
            txtLINNO.requestFocus();
            setMSG("Enter the Line No.. ",'N');
        }
        else if(M_objSOURC == txtLINNO)
        {
            cl_dat.M_btnSAVE_pbst.requestFocus();
            setMSG("Click to generate data file.. ",'N');
        }
     }
     if (L_KE.getKeyCode()== L_KE.VK_F1)
     {
    	if(M_objSOURC == txtPRDCD)
		{
			cl_dat.M_flgHELPFL_pbst = true;
			strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			strQCATP =M_strSBSCD.substring(0,2);
		    M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST where isnull(PR_STSFL,'') <> 'X' "+
                          " AND PR_PRDTP ='"+strPRDTP +"'"+		    
		                  " order by PR_PRDDS  ";
		    M_strHLPFLD = "txtPRDCD";
		    staHLPHD[0] = " Product Code ";
		    staHLPHD[1] = " Grade ";
			cl_hlp(M_strSQLQRY,2,1,staHLPHD,2,"CT");
		}
		else if(M_objSOURC == txtLINNO)
		{
			cl_dat.M_flgHELPFL_pbst = true;
			strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			strQCATP = M_strSBSCD.substring(2,4);
			staHLPHD[0] = "Line No.";
			staHLPHD[1] = "Description";	
			M_strSQLQRY = "SELECT cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp =  ";
			M_strSQLQRY = M_strSQLQRY + "'SYS'" + " AND cmt_cgstp = ";
			M_strSQLQRY = M_strSQLQRY + "'PRXXLIN'";
			M_strSQLQRY += " AND CMT_CCSVL = " + "'"+strPRDTP + "'";			    
			M_strHLPFLD = "txtPRDCD";
			txtLINNO.setEnabled(false);
			M_strHLPFLD = "txtLINNO";
		  	cl_hlp(M_strSQLQRY,2,1,staHLPHD,2,"CT");
		}
 	 }
}
void exeHLPOK()
{
    super.exeHLPOK();
     if(M_strHLPFLD.equals("txtPRDCD"))
     {
         txtPRDCD.setText(cl_dat.M_strHLPSTR_pbst);
        // txtPRDCD.setEnabled(true);
         txtPRDCD.requestFocus();
     }
	 else if (M_strHLPFLD.equals("txtLINNO"))
     {
         txtLINNO.setText(cl_dat.M_strHLPSTR_pbst);
         txtLINNO.setEnabled(true);
         txtLINNO.requestFocus();
     }
     cl_dat.M_flgHELPFL_pbst = false;
}
private boolean vldLINNO() 
{
    try
    {
        int L_intCOUNT =0;
     	M_strSQLQRY ="select count(*)L_COUNT from co_cdtrn where CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'PRXXLIN' AND CMT_CODCD =";
    	M_strSQLQRY +="'"+ txtLINNO.getText().trim()+"'";
    	M_strSQLQRY +=" AND CMT_CCSVL = '"+ cmbPRDTP.getSelectedItem().toString().substring(0,2)+"'";
    	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    	if(M_rstRSSET !=null)
    	{
    	    if(M_rstRSSET.next())
    	    {
    	        L_intCOUNT = M_rstRSSET.getInt("L_COUNT");
    	    }
    	    if(L_intCOUNT == 0)
    	     {
    	        setMSG("Invalid Line No...Enter some valid Line No ", 'E');
    	        return false;
    	     }
    	     else return true;
     	}
    }
    catch(Exception L_E)
    {
        setMSG(L_E,"vldLINNO");        
        return false;
    }
    return true;
}
Connection setCONFTB(String P_strDIRNM)
{
	String L_strURLST ="";
    L_strURLST = "jdbc:odbc:Visual FoxPro Tables;SourceDB = " + P_strDIRNM;
	try
	{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        conSPBKA = DriverManager.getConnection(L_strURLST,"","");
	    stmSTBK1 = conSPBKA.createStatement();
	}
	catch(ClassNotFoundException L_CNFE){
		setMSG(L_CNFE,"setCONFTB");
	}
	catch(SQLException L_SQLE){
	    setMSG(L_SQLE,"setCONFTB");
	}
	return conSPBKA;
}
void exePRINT()
{
    if(!vldDATA())
        return;
    try
	{
       if(rdbDBFRP.isSelected())
       {
           cpyDBFFL();
           getLOTRNG();
       }
       else if(rdbXLSRP.isSelected())
       {
           strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpmtb.xls";				
           getLOTRNG1();
           if(dosREPORT !=null)
             dosREPORT.close();
           if(fosREPORT !=null)
             fosREPORT.close();
           if(intRECCT == 0)
    	   {
    	    	setMSG("Data not found for the given Inputs..",'E');
    			return;
    	   }
           /*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
           {					
              doPRINT(strFILNM);
           }*/
           if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
           {
		   System.out.println("Show");	
               Process p = null;			
               Runtime r = Runtime.getRuntime();
               //p  = r.exec("C:\\windows\\Excel.exe " + strFILNM); 
			   p  = r.exec("D:\\Program Files (x86)\\Microsoft Office\\Office12\\excel.exe "+ strFILNM); 
		   System.out.println("Show "+strFILNM);	
           }				
       }
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"exePRINT");
	}	
}
void cpyDBFFL()
{

	boolean L_flgEXTVL = false;
	Process p = null;
	this.setCursor(cl_dat.M_curWTSTS_pbst);
 	L_flgEXTVL = false;	
	Runtime r = Runtime.getRuntime();
	try
	{
		    if(conSPBKA !=null)
		        conSPBKA.close();
		    setCONFTB("C:\\Reports");
		    File L_TMPFL = new File("c:\\reports\\qc_rpmtb.dbf");
			if(L_TMPFL.exists())
					L_TMPFL.delete();
			p  = r.exec("c:\\windows\\xcopy.exe" + " f:\\foxdat\\0102\\datatfr\\qcadata\\qc_rpmtb.dbf " + "c:\\reports"); 
			while(!L_flgEXTVL)
			{
    			try
    			{
    				L_flgEXTVL = true;
    				p.exitValue();
    			}catch(Exception L_E)
    			{
    				L_flgEXTVL = false;
    			}
			}
		}
		catch(Exception L_E)
		{
	        setMSG(L_E,"cpyDBFFL");
		}
		p.destroy();

}
boolean vldDATA()
{
   if(txtPRDCD.getText().trim().length() ==0) 
   {
	   setMSG("Enter the Product Code ..",'N');
	   txtPRDCD.requestFocus();
	   return false;
   }
   else if(txtLINNO.getText().trim().length() != 2) 
   {
	   setMSG("Enter the two digit Line Number ..",'N');
	   txtLINNO.requestFocus();
	   return false;
   }
   else if(txtPRDCD.getText().trim().length() != 10) 
   {
	   setMSG("Enter the 10 digit Product code ..",'N');
	   txtPRDCD.requestFocus();
	   return false;
   }
   if(txtFRDAT.getText().trim().length() ==0) 
   {
	   setMSG("Enter the From Value in Date Range ..",'N');
	   txtFRDAT.requestFocus();
	   return false;
   }
   if(txtTODAT.getText().trim().length() ==0) 
   {
	   setMSG("Enter the To Value in Date Range ..",'N');
	   txtTODAT.requestFocus();
	   return false;
   }
   return true; 
}
class INPVF extends InputVerifier
{
    public boolean verify(JComponent input)
    {
       try
       {
            if(((JTextField)input).getText().length() ==0)
                return true;
            if(input == txtFRDAT)
            {
                if(M_fmtLCDAT.parse(txtFRDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)		
                {
                    setMSG("From Date can not be greater than current date..",'E');
                    return false;
                }
                if(txtTODAT.getText().length() >0)
                if(M_fmtLCDAT.parse(txtFRDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)		
                {
                    setMSG("From Date can not be greater than To date..",'E');
                    return false;
                }
            }
            if(input == txtTODAT)
            {       
               if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)		
                {
                    setMSG("To Date can not be greater than current date..",'E');
                    return false;
                }
                if(txtFRDAT.getText().length() >0)
                if(M_fmtLCDAT.parse(txtFRDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)		
                {
                    setMSG("From Date can not be greater than To date..",'E');
                    return false;
                }
            }
            if(input == txtLINNO)
            { 
                if(!vldLINNO())
                {
                    setMSG("Invalid Line No..",'E');
                    return false;             
                }
            }
            if(input == txtPRDCD)
            { 
                if(!vldPRDCD(txtPRDCD.getText().trim()))
                {
                    setMSG("Invalid Product Code..",'E');
                    return false;             
                }   
            }
       }
       catch(Exception L_E)
       {
            setMSG(L_E,"Input Verifier"); 
       }
        return true;
    }
}
}
