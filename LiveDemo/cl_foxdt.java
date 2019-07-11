/*
PROGRAM TO TRANSFER DATA FROM FOX PRO TO DB2 FOR PERTICUALR TABLE
*/
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.Color;
import java.sql.SQLException;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

class cl_foxdt extends cl_pbase implements ActionListener{ 
	JButton btnEXP01,btnEXP02;
	Statement stmSTBK1;
	Connection conSPBKA;
	cl_foxdt(){
		super(1);
		btnEXP01 = new JButton("FP to DB2");
		btnEXP02 = new JButton("DATA in DB2");
		setMatrix(20,8);
		add(btnEXP01,3,1,1,2,this,'L');
		add(btnEXP02,4,1,1,2,this,'L');
	}
	public void actionPerformed(ActionEvent L_AE){
		super.actionPerformed(L_AE);
		if(L_AE.getSource()==btnEXP01){
			//setCONFTB("C:\\Abhi\\db");
			setCONFTB("C:\\reports");
			System.out.println("setconftb");
		}
		if(L_AE.getSource()==btnEXP02){
			try{
				M_strSQLQRY="Select * From CO_CTPTR";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				if (M_rstRSSET != null){
					System.out.println("Not Null");
				}
				int i=0;
				while(M_rstRSSET.next()){
					System.out.print("  "+i+":"+M_rstRSSET.getString("ctp_prttp"));
					i++;
				}
			}
			catch(SQLException L_SQLE){
				System.out.print("Database not found");
			}
		}
	}
	public Connection setCONFTB(String LP_PTHWD){
		String L_URLSTR ="";
        L_URLSTR = "jdbc:odbc:Visual FoxPro Tables;SourceDB = " + LP_PTHWD;
		try{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            conSPBKA = DriverManager.getConnection(L_URLSTR,"","");
			System.out.println(" connection "+conSPBKA);
            stmSTBK1 = conSPBKA.createStatement();
			System.out.println(" Statement created..."+stmSTBK1);
			//tfrCTPBV();
		//	tfrPOMST("PO_POMST");	
		//	tfrPOTRN("PO_POTRN");	
		//	tfrPODEL("PO_PODEL");	
		//	tfrPOMST("PO_POMAM");	
		//	tfrPOTRN("PO_POTAM");	
		//	tfrPODEL("PO_PODAM");	
		//	tfrTXMST();	
		//	tfrTXTRN();	
		//	crtPSLDBF();
		//	crtMNDBF();
		//	tfrAVMST();
		}
		catch(ClassNotFoundException L_CNFE){
			System.out.println("Connectiob not found");
		}
		catch(SQLException L_SQLE){
			System.out.print("Database not found "+M_strSQLQRY);
		}
		return conSPBKA;
	}
	private void tfrCTPBV()
	{
		try
		{
			M_rstRSSET = stmSTBK1.executeQuery("select * from Sp_ctpbv");
			System.out.println(" RESULT SET........"+M_rstRSSET);
			if (M_rstRSSET != null){
				System.out.println(" Data base is not null");
				int i=0,j=0;
				while(M_rstRSSET.next()){
					cl_dat.M_flgLCUPD_pbst = true;
					M_strSQLQRY="insert into CO_CTPTR (CTP_PRTTP,CTP_PRTCD,CTP_MATCD,CTP_CLSCD,CTP_LPONO,CTP_LPODT,CTP_LPORT,CTP_LUSBY,CTP_LUPDT) values("
						+"'"+nvlSTRVL(M_rstRSSET.getString("ctt_type"),"")+"',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("ctt_code"),"")+"',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("ctt_matcd"),"")+"',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("ctt_class"),"")+"',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("ctt_lpono"),"")+"',"
						+"'"+M_fmtDBDAT.format(M_rstRSSET.getDate("ctt_lpodt"))+"',"
						+nvlSTRVL(M_rstRSSET.getString("ctt_lrate"),"0")+","
						+"'"+nvlSTRVL(M_rstRSSET.getString("ctt_luid"),"")+"',"
						+"'"+M_fmtDBDAT.format(M_rstRSSET.getDate("ctt_lupd"))+"')";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					if(cl_dat.exeDBCMT("exeDB2"))
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("Commit failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ctpbv");
		}
	}
	private void tfrAVMST()
	{
		try
		{
			M_rstRSSET = stmSTBK1.executeQuery("select * from Sp_avmst");
			System.out.println(" RESULT SET........"+M_rstRSSET);
			if (M_rstRSSET != null){
				System.out.println(" Data base is not null");
				int i=0,j=0;
				while(M_rstRSSET.next()){
					cl_dat.M_flgLCUPD_pbst = true;
					M_strSQLQRY="insert into MM_AVMST(AV_PRTTP,AV_PRTCD,AV_GRPCD) values("
						+"'"+nvlSTRVL(M_rstRSSET.getString("av_cttype"),"")+"',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("av_ctcode"),"")+"',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("av_matgr"),"")+"')";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					if(cl_dat.exeDBCMT("exeDB2"))
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("Commit failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ctpbv");
		}
	}
	
	private void tfrPOMST(String L_TBLNM)
	{
		try
		{
			if(L_TBLNM.equalsIgnoreCase("po_pomst"))
				M_rstRSSET = stmSTBK1.executeQuery("select * from PO_POMST order by po_pono");
			else if(L_TBLNM.equalsIgnoreCase("po_pomam"))
				M_rstRSSET = stmSTBK1.executeQuery("select * from PO_POMAM order by po_pono");
			System.out.println(" RESULT SET........"+M_rstRSSET);
			if (M_rstRSSET != null){
				System.out.println(" Data base is not null");
				int i=0,j=0;
				while(M_rstRSSET.next()){
					cl_dat.M_flgLCUPD_pbst = true;
					if(L_TBLNM.equalsIgnoreCase("po_pomst"))
					M_strSQLQRY="insert into MM_POMST(PO_STRTP,PO_PORTP,PO_PORNO,PO_PORDT,PO_AMDNO,PO_AMDDT,";
					else if(L_TBLNM.equalsIgnoreCase("po_pomam"))
						M_strSQLQRY="insert into MM_POMAM(PO_STRTP,PO_PORTP,PO_PORNO,PO_PORDT,PO_AMDNO,PO_AMDDT,";
					
					M_strSQLQRY+="PO_BUYCD,PO_QTNTP,PO_QTNNO,PO_EFFDT,PO_CMPDT,PO_VENTP,PO_VENCD,PO_CURCD,PO_EXGRT,"+
								"PO_PORVL,PO_SHRDS,PO_AUTBY,PO_AUTDT,PO_INSBY,PO_TRNFL,"+
								//"PO_PORVL,PO_SHRDS,PO_PREBY,PO_PREDT,PO_CHKBY,PO_CHKDT,PO_AUTBY,PO_AUTDT,PO_INSBY,PO_TRNFL,"+
								//"PO_STSFL,PO_LUSBY,PO_LUPDT,PO_MMSBS,PO_DELTP,PO_APTVL,PO_PMTFL,PO_DSTCD,PO_MOTCD,PO_PMTRF,"+
								"PO_STSFL,PO_LUSBY,PO_LUPDT,PO_MMSBS)VALUES(";
								//"PO_INSRF,PO_FORRF) VALUES(";
					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PO_STTYPE"),"")+"',"+
								  "' '," // PORTP
								+"'"+nvlSTRVL(M_rstRSSET.getString("PO_PONO"),"")+"',"
								+"'"+M_fmtDBDAT.format(M_rstRSSET.getDate("PO_PODT"))+"',"
								+"'"+nvlSTRVL(M_rstRSSET.getString("PO_AMDNO"),"")+"',"
								+"'"+M_fmtDBDAT.format(M_rstRSSET.getDate("PO_AMDDT"))+"',"
								+"'"+nvlSTRVL(M_rstRSSET.getString("PO_BUYCD"),"")+"',"  
								+"' ',"  // QTNTP
								+"'"+nvlSTRVL(M_rstRSSET.getString("PO_QTNNO"),"")+"',"
								+"'"+M_fmtDBDAT.format(M_rstRSSET.getDate("PO_EFFDT"))+"',"
								+"'"+M_fmtDBDAT.format(M_rstRSSET.getDate("PO_CMPDT"))+"',"
								+"' ',"  // VENTP
								+"'"+nvlSTRVL(M_rstRSSET.getString("PO_VENCD"),"")+"',"  
								+"'"+nvlSTRVL(M_rstRSSET.getString("PO_CURCD"),"")+"',"  
								+nvlSTRVL(M_rstRSSET.getString("PO_EXCHRT"),"0")+","
								+nvlSTRVL(M_rstRSSET.getString("PO_VALUE"),"0")+","
								+"'"+nvlSTRVL(M_rstRSSET.getString("PO_SHRTDES"),"")+"',"  
								+"'"+nvlSTRVL(M_rstRSSET.getString("PO_AUTHBY"),"")+"',"  
								+"'"+M_fmtDBDAT.format(M_rstRSSET.getDate("PO_AUTHDT"))+"',"  
								+"'"+nvlSTRVL(M_rstRSSET.getString("PO_INSPBY"),"")+"',"  
								+"'0'," // TRNFL
								+"'"+nvlSTRVL(M_rstRSSET.getString("PO_STATUS"),"")+"',"  
								+"'"+nvlSTRVL(M_rstRSSET.getString("PO_LUID"),"")+"',"
								+"'"+M_fmtDBDAT.format(M_rstRSSET.getDate("PO_LUPD"))+"',"
								+"'01"+nvlSTRVL(M_rstRSSET.getString("PO_STTYPE"),"")+"00')"; 
								  
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					if(cl_dat.exeDBCMT("exeDB2"))
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("Commit failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
				}
			}
			if(L_TBLNM.equalsIgnoreCase("PO_POMST"))
			M_strSQLQRY ="Update MM_POMST ";
			else
				M_strSQLQRY ="Update MM_POMAM ";
			M_strSQLQRY +="set PO_PORDT = null where PO_PORDT <'01/01/1900'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.exeDBCMT("exePO");
			cl_dat.M_flgLCUPD_pbst = true;
			if(L_TBLNM.equalsIgnoreCase("PO_POMST"))
			M_strSQLQRY ="Update MM_POMST ";
			else
				M_strSQLQRY ="Update MM_POMAM ";
			M_strSQLQRY +="set PO_AMDDT = null where PO_AMDDT <'01/01/1900'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.exeDBCMT("exePO");
			cl_dat.M_flgLCUPD_pbst = true;
			
			if(L_TBLNM.equalsIgnoreCase("PO_POMST"))
			M_strSQLQRY ="Update MM_POMST";
			else
				M_strSQLQRY ="Update MM_POMAM ";
			M_strSQLQRY +="set PO_EFFDT = null where PO_EFFDT <'01/01/1900'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.exeDBCMT("exePO");
			cl_dat.M_flgLCUPD_pbst = true;
			
			if(L_TBLNM.equalsIgnoreCase("PO_POMST"))
			M_strSQLQRY ="Update MM_POMST ";
			else
				M_strSQLQRY ="Update MM_POMAM ";
			M_strSQLQRY +="set PO_CMPDT = null where PO_CMPDT <'01/01/1900'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.exeDBCMT("exePO");
			cl_dat.M_flgLCUPD_pbst = true;
			
			if(L_TBLNM.equalsIgnoreCase("PO_POMST"))
			M_strSQLQRY ="Update MM_POMST ";
			else
				M_strSQLQRY ="Update MM_POMAM ";
			M_strSQLQRY +="set PO_AUTDT = null where PO_AUTDT <'01/01/1900'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.exeDBCMT("exePO");
			cl_dat.M_flgLCUPD_pbst = true;
			
			if(L_TBLNM.equalsIgnoreCase("PO_POMST"))
			M_strSQLQRY ="Update MM_POMST ";
			else
				M_strSQLQRY ="Update MM_POMAM ";
			M_strSQLQRY +="set PO_LUPDT = null where PO_LUPDT <'01/01/1900'";
			
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.exeDBCMT("exePO");
			cl_dat.M_flgLCUPD_pbst = true;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ctpbv");
		}
	}
	private void tfrPOTRN(String L_TBLNM)
	{
		try
		{
			if(L_TBLNM.equalsIgnoreCase("po_potrn"))
				M_rstRSSET = stmSTBK1.executeQuery("select * from po_potrn");
			else if(L_TBLNM.equalsIgnoreCase("po_potam"))
				M_rstRSSET = stmSTBK1.executeQuery("select * from po_potam");
			
			System.out.println(" RESULT SET........"+M_rstRSSET);
			if (M_rstRSSET != null){
				System.out.println(" Data base is not null");
				int i=0,j=0;
				while(M_rstRSSET.next()){
					cl_dat.M_flgLCUPD_pbst = true;
					if(L_TBLNM.equalsIgnoreCase("po_potrn"))
						M_strSQLQRY="insert into MM_POTRN (POT_STRTP,POT_PORTP,POT_PORNO,POT_MATCD,";
					else if(L_TBLNM.equalsIgnoreCase("po_potam"))
						M_strSQLQRY="insert into MM_POTAM (POT_STRTP,POT_PORTP,POT_PORNO,POT_MATCD,";
					M_strSQLQRY+="POT_AMDNO,POT_RMSRL,POT_INDTP,POT_INDNO,POT_DPTCD,POT_UOMCD,"+
								"POT_UCNVL,POT_PORRT,POT_PERRT,POT_PORQT,POT_ACPQT,POT_FRCQT,POT_ITVAL,"+
								"POT_DELCT,POT_TRNFL,POT_STSFL,POT_LUSBY,POT_LUPDT,POT_MMSBS,POT_MATDS) VALUES("
						+"'"+nvlSTRVL(M_rstRSSET.getString("POT_STTYPE"),"")+"',"
						+"' ',"		
						+"'"+nvlSTRVL(M_rstRSSET.getString("POT_PONO"),"")+"',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("POT_MATCD"),"")+"',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("POT_AMDNO"),"")+"',"
						+nvlSTRVL(M_rstRSSET.getString("POT_RMSRL"),"0")+","
						+"' ',"		
						+"'"+nvlSTRVL(M_rstRSSET.getString("POT_INDNO"),"")+"',"		
						+"'"+nvlSTRVL(M_rstRSSET.getString("POT_DPTCD"),"")+"',"				
						+"'"+nvlSTRVL(M_rstRSSET.getString("POT_UOM"),"")+"',"				
						+nvlSTRVL(M_rstRSSET.getString("POT_UOMCNV"),"0")+","		
						+nvlSTRVL(M_rstRSSET.getString("POT_RATE"),"0")+","		
						+nvlSTRVL(M_rstRSSET.getString("POT_RTPER"),"0")+","		
						+nvlSTRVL(M_rstRSSET.getString("POT_ORDQTY"),"0")+","		
						+nvlSTRVL(M_rstRSSET.getString("POT_ACPQTY"),"0")+","		
						+nvlSTRVL(M_rstRSSET.getString("POT_FCQTY"),"0")+","		
						+nvlSTRVL(M_rstRSSET.getString("POT_ITVAL"),"0")+","				
						+nvlSTRVL(M_rstRSSET.getString("POT_NODLS"),"0")+","		
						+"'0',"				
						+"'"+nvlSTRVL(M_rstRSSET.getString("POT_STATUS"),"")+"',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("POT_LUID"),"")+"',"
						+"'"+M_fmtDBDAT.format(M_rstRSSET.getDate("POT_LUPD"))+"',"
						+"'01"+nvlSTRVL(M_rstRSSET.getString("POT_STTYPE"),"")+"00'," 		
						+"'"+nvlSTRVL(M_rstRSSET.getString("POT_MATDES"),"")+"')";
						
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					if(cl_dat.exeDBCMT("exeDB2"))
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("Commit failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
				}
					
			}
			/*		if(L_TBLNM.equalsIgnoreCase("PO_POTRN"))
				M_strSQLQRY ="Update MM_POTRN ";
				else 
					M_strSQLQRY ="Update MM_POTAM ";
				M_strSQLQRY +="set POT_LUPDT = null where pot_lupdt <'01/01/1900'";
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				cl_dat.exeDBCMT("exeDB2");
				// department code updating
				cl_dat.M_flgLCUPD_pbst = true;*/
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ctpbv");
		}
	}
	private void tfrPODEL(String L_TBLNM)
	{
		try
		{
			if(L_TBLNM.equalsIgnoreCase("po_podel"))
				M_rstRSSET = stmSTBK1.executeQuery("select * from po_podel");
			else if(L_TBLNM.equalsIgnoreCase("po_podam"))
				M_rstRSSET = stmSTBK1.executeQuery("select * from po_podam");
			System.out.println(" RESULT SET........"+M_rstRSSET);
			if (M_rstRSSET != null){
				System.out.println(" Data base is not null");
				int i=0,j=0;
				while(M_rstRSSET.next()){
					cl_dat.M_flgLCUPD_pbst = true;
					if(L_TBLNM.equalsIgnoreCase("po_podel"))
						M_strSQLQRY="insert into MM_PODEL (POD_STRTP,POD_PORTP,POD_PORNO,POD_MATCD,POD_EDLDT,POD_EDLQT,POD_ADLQT,POD_AMDNO,POD_TRNFL,POD_STSFL,POD_LUSBY,POD_LUPDT,POD_ADLDT) values(";
					else if (L_TBLNM.equalsIgnoreCase("po_podam"))			
							 M_strSQLQRY="insert into MM_PODAM (POD_STRTP,POD_PORTP,POD_PORNO,POD_MATCD,POD_EDLDT,POD_EDLQT,POD_ADLQT,POD_AMDNO,POD_TRNFL,POD_STSFL,POD_LUSBY,POD_LUPDT,POD_ADLDT) values(";
						M_strSQLQRY+="'"+nvlSTRVL(M_rstRSSET.getString("POD_STTYPE"),"")+"',"
						+"' ',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("POD_PONO"),"")+"',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("POD_MATCD"),"")+"',"
						+"'"+M_fmtDBDAT.format(M_rstRSSET.getDate("POD_DELDT"))+"',"
						+nvlSTRVL(M_rstRSSET.getString("POD_DELQTY"),"0")+","
						+nvlSTRVL(M_rstRSSET.getString("POD_ACPQTY"),"0")+","
						+"'"+nvlSTRVL(M_rstRSSET.getString("POD_AMDNO"),"")+"',"
						+"'0',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("POD_STATUS"),"")+"',"
						+"'"+nvlSTRVL(M_rstRSSET.getString("POD_LUID"),"")+"',"
						+"'"+M_fmtDBDAT.format(M_rstRSSET.getDate("POD_LUPD"))+"',null)";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					if(cl_dat.exeDBCMT("exeDB2"))
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("Commit failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
				}
			}
			/*if(L_TBLNM.equalsIgnoreCase("PO_PODEL"))
			M_strSQLQRY ="Update MM_POdel ";
			else
				M_strSQLQRY ="Update MM_PODAM ";
			M_strSQLQRY +="set POd_LUPDT = null where pod_lupdt <'01/01/1900'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"");
			cl_dat.exeDBCMT("exeDB2");*/
			cl_dat.M_flgLCUPD_pbst = true;
			if(L_TBLNM.equalsIgnoreCase("PO_PODEL"))
			M_strSQLQRY ="Update MM_POdel ";
			else
				M_strSQLQRY ="Update MM_PODAM ";
			M_strSQLQRY+="set POD_EDLDT = null where pod_EDLdt <'01/01/1900'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"");
			cl_dat.exeDBCMT("exeDB2");
			cl_dat.M_flgLCUPD_pbst = true;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ctpbv");
		}
	}
	private void tfrTXMST()
	{
		try
		{
			java.sql.ResultSet L_rstRSSET ;
			String L_STRTP,L_PORNO,L_AMDNO,L_TAXCD,L_TXCOD,L_TAXFL,L_TAXVL;
			M_rstRSSET = stmSTBK1.executeQuery("select * from PO_TXMST order by TX_STTYPE,TX_pono");
			System.out.println(" RESULT SET........"+M_rstRSSET);
			if (M_rstRSSET != null){
				System.out.println(" Data base is not null");
				int i=0,j=0;
				while(M_rstRSSET.next()){
					cl_dat.M_flgLCUPD_pbst = true;
					L_STRTP = nvlSTRVL(M_rstRSSET.getString("tx_sttype"),"");
					L_PORNO = nvlSTRVL(M_rstRSSET.getString("tx_pono"),"");
					L_AMDNO = nvlSTRVL(M_rstRSSET.getString("tx_AMDno"),"");
					L_TXCOD= (M_rstRSSET.getString("tx_TXCOD"));
				//	L_TAXCD = "TX_"+L_TXCOD+"VL";
					L_TAXFL = "TX_"+L_TXCOD+"FL";
					L_TAXVL = "TX_"+L_TXCOD+"VL";
				/*	M_strSQLQRY ="SELECT count(*) L_CNT from co_txdoc where tx_syscd ='MM' and TX_DOCTP ='POR' and tx_SBSTP ='"+L_STRTP +"'";
					M_strSQLQRY += " AND TX_DOCNO ='"+L_PORNO +"'";
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(L_rstRSSET !=null)
						if(L_rstRSSET.next())
						{
							if(L_rstRSSET.getInt("L_CNT")==0)
							{
								M_strSQLQRY="insert into CO_TXDOC(TX_SYSCD,TX_DOCTP,TX_PRDCD,TX_SBSTP,TX_DOCNO,TX_AMDNO,TX_LUSBY,TX_LUPDT)VALUES('MM','POR','XXXXXXXXXX','"+L_STRTP+"','";
								M_strSQLQRY+=L_PORNO +"','"+L_AMDNO+"',";
								M_strSQLQRY+= "'"+nvlSTRVL(M_rstRSSET.getString("TX_LUID"),"")+"',";
								M_strSQLQRY+="'"+M_fmtDBDAT.format(M_rstRSSET.getDate("TX_LUPD"))+"')"	;
							//	System.out.println(M_strSQLQRY);
								cl_dat.exeDBCMT("exeDB2");
							}
					/*		M_strSQLQRY ="UPDATE CO_TXDOC SET "+L_TAXFL +"='"+M_rstRSSET.getString("tx_TAMTP")+"',"
											+L_TAXVL +"="+M_rstRSSET.getString("tx_TAMT")+
										 " WHERE TX_SBSTP ='"+L_STRTP +"' AND TX_DOCTP ='POR' AND TX_DOCNO ='"+L_PORNO +"'";
															cl_dat.exeDBCMT("exeDB2");
							//System.out.println(M_strSQLQRY);
						}*/
					
					
					M_strSQLQRY ="UPDATE CO_TXDOC SET "+L_TAXFL +"='"+M_rstRSSET.getString("tx_TAMTP")+"',"
											+L_TAXVL +"="+M_rstRSSET.getString("tx_TAMT");
								 if(L_TAXFL.equals("TX_STXFL"))
									M_strSQLQRY += ",TX_STXDS ='"+M_rstRSSET.getString("tx_txdes") +"'";
								 if(L_TAXFL.equals("TX_OTHFL"))
									M_strSQLQRY += ",TX_OTHDS ='"+M_rstRSSET.getString("tx_txdes") +"'";
					M_strSQLQRY +=	 " WHERE TX_SBSTP ='"+L_STRTP +"' AND TX_DOCTP ='POR' AND TX_DOCNO ='"+L_PORNO +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					if(cl_dat.exeDBCMT("exeDB2"))
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("Commit failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
				}
			}
			cl_dat.M_flgLCUPD_pbst = true;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ctpbv");
		}
	}
private void tfrTXTRN()
	{
		try
		{
			java.sql.ResultSet L_rstRSSET ;
			String L_STRTP,L_PORNO,L_AMDNO,L_TAXCD,L_TXCOD,L_TAXFL,L_TAXVL,L_MATCD;
			M_rstRSSET = stmSTBK1.executeQuery("select * from PO_TXtrn order by TXt_STTYPE,TXt_pono,TXT_MATCD");
			System.out.println(" RESULT SET........"+M_rstRSSET);
			if (M_rstRSSET != null){
				System.out.println(" Data base is not null");
				int i=0,j=0;
				while(M_rstRSSET.next()){
					cl_dat.M_flgLCUPD_pbst = true;
					L_STRTP = nvlSTRVL(M_rstRSSET.getString("txt_sttype"),"");
					L_PORNO = nvlSTRVL(M_rstRSSET.getString("txt_pono"),"");
					L_AMDNO = nvlSTRVL(M_rstRSSET.getString("txt_AMDno"),"");
					L_MATCD = nvlSTRVL(M_rstRSSET.getString("txt_MATCD"),"");
					L_TXCOD= (M_rstRSSET.getString("txt_TXCOD"));
				//	L_TAXCD = "TX_"+L_TXCOD+"VL";
					L_TAXFL = "TX_"+L_TXCOD+"FL";
					L_TAXVL = "TX_"+L_TXCOD+"VL";
					M_strSQLQRY ="SELECT count(*) L_CNT from co_txdoc where tx_syscd ='MM' and TX_DOCTP ='POR' and tx_SBSTP ='"+L_STRTP +"'";
					M_strSQLQRY += " AND TX_DOCNO ='"+L_PORNO +"'";
					M_strSQLQRY += " AND TX_PRDCD ='"+L_MATCD +"'";
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(L_rstRSSET !=null)
						if(L_rstRSSET.next())
						{
							if(L_rstRSSET.getInt("L_CNT")==0)
							{
								M_strSQLQRY="insert into CO_TXDOC(TX_SYSCD,TX_DOCTP,TX_PRDCD,TX_SBSTP,TX_DOCNO,TX_AMDNO,TX_LUSBY,TX_LUPDT)VALUES('MM','POR','"
											+L_MATCD+"','"
											+L_STRTP+"','";
								M_strSQLQRY+=L_PORNO +"','"+L_AMDNO+"',";
								M_strSQLQRY+= "'"+nvlSTRVL(M_rstRSSET.getString("TXt_LUID"),"")+"',";
								M_strSQLQRY+="'"+M_fmtDBDAT.format(M_rstRSSET.getDate("TXt_LUPD"))+"')"	;
							//	System.out.println(M_strSQLQRY);
								//cl_dat.exeDBCMT("exeDB2");
							}
					/*		M_strSQLQRY ="UPDATE CO_TXDOC SET "+L_TAXFL +"='"+M_rstRSSET.getString("tx_TAMTP")+"',"
											+L_TAXVL +"="+M_rstRSSET.getString("tx_TAMT")+
										 " WHERE TX_SBSTP ='"+L_STRTP +"' AND TX_DOCTP ='POR' AND TX_DOCNO ='"+L_PORNO +"'";
															cl_dat.exeDBCMT("exeDB2");
							//System.out.println(M_strSQLQRY);*/
						}
					
					/*
					M_strSQLQRY ="UPDATE CO_TXDOC SET "+L_TAXFL +"='"+M_rstRSSET.getString("tx_TAMTP")+"',"
											+L_TAXVL +"="+M_rstRSSET.getString("tx_TAMT");
								 if(L_TAXFL.equals("TX_STXFL"))
									M_strSQLQRY += ",TX_STXDS ='"+M_rstRSSET.getString("tx_txdes") +"'";
								 if(L_TAXFL.equals("TX_OTHFL"))
									M_strSQLQRY += ",TX_OTHDS ='"+M_rstRSSET.getString("tx_txdes") +"'";
					M_strSQLQRY +=	 " WHERE TX_SBSTP ='"+L_STRTP +"' AND TX_DOCTP ='POR' AND TX_DOCNO ='"+L_PORNO +"'";
					M_strSQLQRY +=" AND TX_PRDCD ='"+L_MATCD +"'"*/
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					if(cl_dat.exeDBCMT("exeDB2"))
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("Commit failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
				}
			}
			cl_dat.M_flgLCUPD_pbst = true;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ctpbv");
		}
	}
private void crtPSLDBF()
	{
		try
		{
			int i=0,j=0;
			java.sql.ResultSet L_rstRSSET ;
			M_strSQLQRY ="SELECT PS_STRTP,PS_DOCNO,PS_DOCDT,PS_MATCD,PS_DOCQT,PS_DOCRT,PS_DOCVL,PS_MODVL,"+
				        "GR_VENCD,GR_VENNM,ST_MATDS FROM MM_PSLYR,MM_GRMST,MM_STMST WHERE PS_DOCTP ='1' "+
					//	" AND PS_DOCDT BETWEEN '07/01/2003' AND '06/30/2004' AND ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_STRTP ='01'"+
						 " AND PS_DOCDT BETWEEN '04/01/2003' AND '03/31/2004' AND ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_STRTP ='01'"+
						" AND PS_STRTP = GR_STRTP AND PS_DOCNO = GR_GRNNO AND PS_MATCD = GR_MATCD AND PS_STRTP ='01' and substr(ps_matcd,1,2) not in('68','69','85','86') ";
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null){
				
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into yr_grdt values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_DOCNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("PS_DOCDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_MATCD"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_DOCRT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_DOCVL"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_MODVL"),"0")+",";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"0")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"')";
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					conSPBKA.commit();
				}
			}
				
			i=0;
			j=0;
			
			M_strSQLQRY ="SELECT GR_STRTP,GR_GRNNO,GR_ACPDT,GR_MATCD,GR_ACPQT,po_porRT,GR_PORVL,GR_MODVL,"+
				        "GR_VENCD,GR_VENNM,ST_MATDS FROM MM_GRMST,mm_pomst,MM_STMST WHERE gr_acpqt > 0 "+
					//	" AND gr_ACPDT BETWEEN '07/01/2003' AND '06/30/2004' AND gr_strtp = po_strtp and gr_porno = po_porno and gr_matcd = po_matcd and ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_STRTP ='01'"+
						" AND gr_ACPDT BETWEEN '04/01/2003' AND '03/31/2004' AND gr_strtp = po_strtp and gr_porno = po_porno and gr_matcd = po_matcd and ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_STRTP ='01'"+
						 " AND substr(gr_matcd,1,2) not in('68','69','85','86') and ifnull(gr_bilvl,0) =0";
						
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null){
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into po_grdt values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_grnNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_ACPDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("po_porRT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_PORVL"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_MODVL"),"0")+",";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"0")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"')";
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					conSPBKA.commit();
				}
			}
		M_strSQLQRY ="SELECT PS_STRTP,PS_DOCNO,PS_DOCDT,PS_MATCD,PS_DOCQT,PS_DOCRT,PS_DOCVL,PS_MODVL,"+
				        "ST_MATDS FROM MM_PSLYR,MM_STMST WHERE PS_DOCTP in('3','5') "+
					//	" AND PS_DOCDT BETWEEN '07/01/2003' AND '06/30/2004' AND ST_STRTP ='01'"+
					 	" AND PS_DOCDT BETWEEN '04/01/2003' AND '03/31/2004' AND ST_STRTP ='01'"+
						" AND PS_STRTP = st_STRTP AND pS_MATCD = st_MATCD AND PS_STRTP ='01' and substr(ps_matcd,1,2) not in('68','69','85','86') ";
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null){
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into yr_smdt values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_DOCNO"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("PS_MATCD"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("PS_DOCDT"))))+"'),";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_DOCQT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("PS_DOCRT"),"0")+",";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"')";
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					conSPBKA.commit();
				}
				
			}
			cl_dat.M_flgLCUPD_pbst = true;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ctpbv");
		}
	}
private void crtMNDBF()
	{
		try
		{
			int i=0,j=0;
			java.sql.ResultSet L_rstRSSET ;
		/*	M_strSQLQRY ="SELECT GR_STRTP,GR_GRNNO,GR_ACPDT,GR_MATCD,GR_ACPQT,GR_GRNRT,GR_BILVL,GR_MODVL,"+
				        "GR_VENCD,GR_VENNM,ST_MATDS FROM MM_GRMST,MM_STMST WHERE "+
						" GR_ACPDT BETWEEN '07/01/2004' AND '07/31/2004' AND ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD AND ST_STRTP ='01'"+
						" and substr(GR_matcd,1,2) not in('68','69','85','86') AND GR_ACPDT IS NOT NULL";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			if (M_rstRSSET != null){
				
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into psl_gr01 values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_ACPDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_GRNRT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_BILVL"),"0")+",";
				//	M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_MODVL"),"0")+",";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"0")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"')";
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					conSPBKA.commit();
				}
			}
		*/
			i=0;
			j=0;
			M_strSQLQRY ="SELECT GR_STRTP,GR_GRNNO,GR_ACPDT,GR_MATCD,GR_ACPQT,GR_GRNRT,GR_BILVL,GR_MODVL,"+
				        "GR_VENCD,GR_VENNM,ST_MATDS FROM MM_GRMST,MM_STMST WHERE "+
						" GR_ACPDT BETWEEN '07/01/2004' AND '07/31/2004' AND ST_STRTP = GR_STRTP AND ST_MATCD = GR_MATCD "+
						" and substr(GR_matcd,1,2) in('68','69') AND GR_ACPDT IS NOT NULL";
			
			System.out.println("called "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			System.out.println(" RESULT SET........"+M_rstRSSET);
			if (M_rstRSSET != null){
				
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into psl_gr07 values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_STRTP"),"")+"',";					
					System.out.println("1");
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_ACPDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_GRNRT"),"0")+",";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_BILVL"),"0")+",";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"0")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"')";
					System.out.println("2 "+M_strSQLQRY);
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					conSPBKA.commit();
				}
			}
			i =0;
			j=0;
		/*	M_strSQLQRY ="SELECT IS_STRTP,IS_ISSNO,DATE(IS_AUTDT)L_ISSDT,IS_MATCD,ST_MATDS,ST_UOMCD,IS_ISSQT FROM MM_ISMST,MM_STMST WHERE "+
						" DATE(IS_AUTDT) BETWEEN '07/01/2004' AND '07/31/2004' AND ST_STRTP = IS_STRTP AND ST_MATCD = IS_MATCD AND ST_STRTP ='01'"+
						" and substr(IS_matcd,1,2) not in('68','69','85','86') AND IS_AUTDT IS NOT NULL";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			System.out.println(" RESULT SET........"+M_rstRSSET);
			if (M_rstRSSET != null){
				
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into psl_IS01 values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_ISSNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("L_ISSDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_uomcd"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("IS_ISSQT"),"0")+")";
					
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					conSPBKA.commit();
				}
			}
			cl_dat.M_flgLCUPD_pbst = true;
			M_strSQLQRY ="SELECT IS_STRTP,IS_ISSNO,DATE(IS_AUTDT)L_ISSDT,IS_MATCD,ST_MATDS,ST_UOMCD,IS_ISSQT FROM MM_ISMST,MM_STMST WHERE "+
						" DATE(IS_AUTDT) BETWEEN '07/01/2004' AND '07/31/2004' AND ST_STRTP = IS_STRTP AND ST_MATCD = IS_MATCD "+
						" and substr(IS_matcd,1,2) in('68','69') AND IS_AUTDT IS NOT NULL";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			System.out.println(" RESULT SET........"+M_rstRSSET);
			if (M_rstRSSET != null){
				
				while(M_rstRSSET.next())
				{
					M_strSQLQRY =  "Insert into psl_IS07 values(";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_STRTP"),"")+"',";					
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_ISSNO"),"")+"',";
					M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("L_ISSDT"))))+"'),";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_matds"),"")+"',";
					M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("st_uomcd"),"")+"',";
					M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("IS_ISSQT"),"0")+")";
					
					if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
					{
						i++;
					}
					else
					{
						j++;
						System.out.println("insert failed");
					}
					setMSG("saved "+i+" not saved "+j,'N');
					conSPBKA.commit();
				}
			}*/
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ctpbv");
		}
	}
	
}
