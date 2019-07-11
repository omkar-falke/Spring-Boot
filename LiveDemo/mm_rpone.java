/*
	ONE TIME REPORTS
    ATC
*/
import java.awt.event.*;
import java.sql.SQLException;
import java.io.IOException;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.sql.ResultSet;import java.sql.Statement;
import java.util.Hashtable;

class mm_rpone extends cl_rbase
{ 
	Statement L_STAT;
	//private JTextField txtSUBGR;
	private JComboBox cmbRPTNM ;
	private String strSTRTP;
	private String strSTRNM;
	private String strSUBGR;
	private String L_strTEMP ="";
	private FileOutputStream F_OUT ;   
    private DataOutputStream D_OUT ;
	
	private final String strFILNM = "C:\\Reports\\mm_rpone.doc";
	Hashtable<String,String> hstDPTCD;	
	mm_rpone()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		
			setMatrix(20,6);
			add(cmbRPTNM = new JComboBox(),2,3,1,4,this,'L');
			cmbRPTNM.addItem("1. Consumption of Surplus items where stock on hand is present");
			cmbRPTNM.addItem("2. Consumption of Items marked as Essential and (Stock controlled/Other)");
			cmbRPTNM.addItem("3. Items in Others Category (In Descending order of value)");
			cmbRPTNM.addItem("4. List of sub sub groups where lead time is 0");
			cmbRPTNM.addItem("5. List of Material code where Sub Group/sub sub group not present");
			cmbRPTNM.addItem("6. List of Header codes with blank Description");
			cmbRPTNM.addItem("7. List of Item codes with blank UOM Code");
			cmbRPTNM.addItem("8. List of Item codes with UOM Descripancies");
			cmbRPTNM.addItem("9. List of Item codes with Blank Description");
			cmbRPTNM.addItem("10. List of Item codes with Blank Owner Code");
			cmbRPTNM.addItem("11. List of Item codes with P.O. rate in group 22");
			cmbRPTNM.addItem("12. List of Item codes with null Item value in stock master");
			cmbRPTNM.addItem("13. List of Item codes in group 76 with Owner Codes");
			cmbRPTNM.addItem("14. Reports required by accounts");
			cmbRPTNM.addItem("15. GRIN List for Batch No. Stock updating");

//			add(new JLabel("For List of items with surplus quantity "),5,3,1,4,this,'L');
//			add(new JLabel("For List of Material code where 3,4 or 5,6 digits are 00 "),3,3,1,4,this,'L');

			
			/*
			add(new JLabel("Sub Group Code"),2,3,1,1,this,'L');
			add(txtSUBGR = new TxtNumLimit(4.0),2,4,1,1,this,'L');
			*/
			L_STAT = cl_dat.M_conSPDBA_pbst.createStatement();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				setENBL(true);
				M_cmbDESTN.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				//txtSUBGR.requestFocus();
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
		if(M_objSOURC == M_cmbDESTN)
		{
			if(M_cmbDESTN.getSelectedIndex() > 0)
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	
	public void getDATA()
	{
		try
		{
			setMSG("Processing.. ",'N');
			setCursor(cl_dat.M_curWTSTS_pbst);
			F_OUT = new FileOutputStream(strFILNM);
			D_OUT = new DataOutputStream(F_OUT);
			prnFMTCHR(D_OUT,M_strNOCPI17);
			prnFMTCHR(D_OUT,M_strCPI10);
			if(cmbRPTNM.getSelectedIndex() ==0)
			    prnFMTCHR(D_OUT,M_strCPI17);
			else
			    prnFMTCHR(D_OUT,M_strCPI12);
			strSUBGR = "";
			int L_intMATCT = 0;
			int L_intGRPCT = 0;
			M_intPAGNO = 1;
			getRPHDR();
		    double L_dblTOTAL =0.0,L_dblITVAL =0.0,L_dblTOTQT =0;
		    String L_strPCATFL ="",L_strCATFL ="",strITCAT="",strPITCAT="";
			/*
			M_strSQLQRY = "SELECT DISTINCT CT_MATCD,CT_MATDS,CT_UOMCD FROM CO_CTMST WHERE "
				+"isnull(CT_STSFL,'') <> 'X' AND CT_CODTP = 'CD' AND SUBSTRING(CT_MATCD,5,2) = "
				+"'00' AND CT_GRPCD NOT IN('99','95') ORDER BY CT_MATCD ";
			*/
			// Query for List of Sub groups not present 	
			/*M_strSQLQRY = "SELECT SUBSTRING(CT_MATCD,1,4) AS MATCD,CT_OWNBY,CT_MATCD,CT_MATDS,CT_UOMCD FROM CO_CTMST WHERE "
				+"SUBSTRING(CT_MATCD,1,4) NOT IN (SELECT SUBSTRING(B.CT_MATCD,1,4) FROM CO_CTMST B "
				+"WHERE SUBSTRING(CT_MATCD,5,6) ='00000A') AND CT_GRPCD NOT IN('51','52','95','99') "
				+"AND SUBSTRING(CT_MATCD,3,2) <>'00' ORDER BY CT_MATCD";*/
			
			/*M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS,CT_UOMCD,CT_OWNBY FROM CO_CTMST WHERE "
				+"SUBSTRING(CT_MATCD,3,2) ='00' AND CT_CODTP <> 'MG'"
				+"AND CT_GRPCD NOT IN('51','52','95','99') "
				+"UNION "		  
				+"SELECT CT_MATCD,CT_MATDS,CT_UOMCD,CT_OWNBY FROM CO_CTMST WHERE "
				+"SUBSTRING(CT_MATCD,5,2) ='00' AND CT_CODTP NOT IN ('MG','SG')"
				+"AND CT_GRPCD NOT IN('51','52','95','99') "
				+"ORDER BY CT_MATCD";*/
			
		//	M_strSQLQRY = "SELECT ST_MATCD,ST_UOMCD,ST_STKFL,ST_VEDFL,ST_STSFL,ST_MATDS,ST_STKQT,ST_MAXLV,ST_SRPQT,CT_OWNBY FROM MM_STMST,CO_CTMST WHERE ST_MATCD = CT_MATCD AND isnull(ST_SRPQT,0) >0 AND ST_STRTP ='01' AND ST_SRPQT > ST_STKQT ORDER BY ST_MATCD ";

			//M_strSQLQRY = "SELECT ST_MATCD,ST_UOMCD,ST_STKFL,ST_VEDFL,ST_STSFL,ST_MATDS,ST_STKQT,ST_MAXLV,ST_SRPQT,CT_OWNBY FROM MM_STMST,CO_CTMST WHERE ST_MATCD = CT_MATCD AND  isnull(ST_SRPQT,0) >0 AND ST_STRTP ='01' AND ST_SRPQT <= ST_STKQT ORDER BY ST_MATCD" ;
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT' AND isnull(CMT_STSFL,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			hstDPTCD = new java.util.Hashtable<String,String>();
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				}
			}
			if(cmbRPTNM.getSelectedIndex() ==0)
			M_strSQLQRY = "select st_matcd,st_matds,st_uomcd,ST_STKFL,ST_VEDFL,ST_STSFL,ST_STKQT,ST_CONQT,ST_PCOQT,ST_MAXLV,ST_SRPQT,STP_OWNBY, sum(is_issqt) L_ISSQT from mm_stprc,mm_stmst "+
						  "left outer join mm_ismst on st_cmpcd=is_cmpcd and st_strtp =is_strtp and st_matcd = is_matcd "+
						  "and IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,is_autdt,101) >='07/01/2005' where st_strtp = stp_strtp and st_matcd = stp_matcd and isnull(st_stkqt,0) >0  and st_strtp ='01' "+
						  "and isnull(st_srpqt,0) >0  group by st_matcd,st_matds,st_uomcd,ST_STKFL,ST_VEDFL,ST_STSFL,ST_STKQT,ST_CONQT,ST_PCOQT,ST_MAXLV,ST_SRPQT,stp_ownby order by stp_ownby,st_matcd";
			if(cmbRPTNM.getSelectedIndex() ==1)
			M_strSQLQRY = "select st_matcd,st_matds,st_uomcd,ST_STKFL,ST_VEDFL,ST_STSFL,ST_STKQT,ST_MAXLV,ST_SRPQT,ST_CONQT,ST_PCOQT,stp_ownby, sum(is_issqt) L_ISSQT from mm_stprc,mm_stmst "+
						  "left outer join mm_ismst on st_strtp =is_strtp and st_matcd = is_matcd "+
						  "and CONVERT(varchar,is_autdt,101) >='07/01/2005' where ST_CMPCD = STP_CMPCD and st_strtp = stp_strtp and st_matcd = stp_matcd and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(st_stkqt,0) >0  and st_strtp ='01' "+
						  "and isnull(st_vedfl,'') ='E' and (isnull(st_stkfl,'')='Y' or isnull(ST_STSFL,'') ='9') group by st_matcd,st_matds,st_uomcd,ST_STKFL,ST_VEDFL,ST_STSFL,ST_STKQT,ST_CONQT,ST_PCOQT,ST_MAXLV,ST_SRPQT,stp_ownby order by stp_ownby,st_matcd";
			if(cmbRPTNM.getSelectedIndex() ==2)
			M_strSQLQRY = "select stp_matcd,stp_matds,stp_uomcd,STP_YCSQT,STP_OWNBY,stp_ycsvl from mm_stprc "+
						  "where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(stp_YCSQT,0) >0  and stp_catfl ='4' and stp_strtp ='01' order by stp_ycsvl DESC";
			
			if(cmbRPTNM.getSelectedIndex() ==3)
				M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS,CT_UOMCD,CT_OWNBY,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM FROM CO_CTMST WHERE "
				+"CT_CODTP = 'SS' and (isnull(CT_ILDTM,0) =0 OR isnull(CT_ELDTM,0) =0 OR isnull(CT_IILTM,0) =0 or isnull(CT_IELTM,0) =0) "
				+"AND CT_GRPCD NOT IN('51','52','95','99') ";
			if(cmbRPTNM.getSelectedIndex() ==4)
			// Query for List of Sub sub groups not present 	
			M_strSQLQRY = "SELECT SUBSTRING(CT_MATCD,1,6) AS MATCD,CT_OWNBY,CT_MATCD,CT_MATDS,CT_UOMCD FROM CO_CTMST WHERE "
				+"SUBSTRING(CT_MATCD,1,6) NOT IN (SELECT SUBSTRING(B.CT_MATCD,1,6) FROM CO_CTMST B "
				+"WHERE SUBSTRING(CT_MATCD,7,4) ='000A') AND CT_GRPCD NOT IN('51','52','95','99') "
				+"AND SUBSTRING(CT_MATCD,5,2) <>'00' ORDER BY CT_MATCD";
		if(cmbRPTNM.getSelectedIndex() ==5)
			// Header codes with blank Desc
			M_strSQLQRY = "SELECT CT_MATCD,CT_OWNBY,CT_MATDS,CT_UOMCD,CT_STSFL FROM CO_CTMST WHERE "
						+ " isnull(CT_MATDS,'') ='' AND CT_CODTP IN('MG','SG','SS')";
		if(cmbRPTNM.getSelectedIndex() ==6)
			// Item codes with blank UOM
			M_strSQLQRY = "SELECT CT_MATCD,CT_OWNBY,CT_MATDS,CT_UOMCD,CT_STSFL FROM CO_CTMST WHERE "
						+ " isnull(CT_UOMCD,'') ='' AND CT_CODTP ='CD'";
		if(cmbRPTNM.getSelectedIndex() ==7)
			// Item codes with Different UOM COde in Catalog and Stock master
			M_strSQLQRY = "SELECT CT_MATCD,CT_OWNBY,CT_MATDS,CT_UOMCD,CT_STSFL,ST_UOMCD FROM CO_CTMST,MM_STMST WHERE "
						  + " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CT_UOMCD,'') <> isnull(ST_UOMCD,'') AND CT_MATCD = ST_MATCD and CT_CODTP ='CD'";
		if(cmbRPTNM.getSelectedIndex() ==8)
			// Item codes with blank UOM
			M_strSQLQRY = "SELECT CT_MATCD,CT_OWNBY,CT_MATDS,CT_UOMCD,CT_STSFL FROM CO_CTMST WHERE "
						+ " isnull(CT_MATDS,'') ='' AND CT_CODTP ='CD'";
		if(cmbRPTNM.getSelectedIndex() ==9)
			// Item codes with blank UOM
			M_strSQLQRY = "SELECT CT_MATCD,CT_OWNBY,CT_MATDS,CT_UOMCD,CT_STSFL FROM CO_CTMST WHERE "
						+ " isnull(CT_OWNBY,'') ='' AND CT_CODTP ='CD' order by ct_matcd";
			if(cmbRPTNM.getSelectedIndex() ==10)
			// Item codes with blank UOM
			M_strSQLQRY = "select ct_matcd,ct_uomcd,ct_matds,ct_lvlrf,ct_prtno,ct_pport from co_ctmst where ct_grpcd ='22' and ct_codtp ='CD' order by ct_matcd" ;
			
			if(cmbRPTNM.getSelectedIndex() ==11)
			M_strSQLQRY = "select st_matcd,st_matds,st_uomcd,ST_STKQT from mm_stmst "+
						  "where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(st_stkQT,0) >0  AND isnull(ST_PPOVL,0) =0 and st_strtp ='01' order by st_matcd ";
	    	if(cmbRPTNM.getSelectedIndex() ==12)
			// Item codes with blank UOM
			M_strSQLQRY = "SELECT CT_MATCD,CT_OWNBY,CT_MATDS,CT_UOMCD,CT_STSFL FROM CO_CTMST WHERE "
						+ " CT_GRPCD ='76' AND CT_CODTP ='CD' UNION "
                        + "SELECT CT_MATCD,CT_OWNBY,CT_MATDS,CT_UOMCD,CT_STSFL FROM CO_CTMST WHERE "
						+ " isnull(CT_OWNBY,'') ='' AND CT_CODTP ='CD' and ct_grpcd not in('51','52','53','54','95','99') order by ct_matcd";
			if(cmbRPTNM.getSelectedIndex() ==13)
	//		M_strSQLQRY = "select stp_matcd,stp_uomcd,stp_matds,STP_YOSQT,stp_YOSVL,STP_CATFL from mm_stprc "+
	//			  "where stp_strtp = '01' and isnull(stp_yosqt,0) >0 and isnull(stp_catfl,'') in('2','3','4') AND STP_YOSVL >= 50000 order by stp_catfl,stp_yosvl desc,stp_matcd ";
	//    	M_strSQLQRY = "select stp_matcd,stp_uomcd,stp_matds,STP_YCSQT,stp_YCSVL,STP_CATFL,stp_ownby,st_ppovl,st_pgrdt from mm_stprc,mm_stmst "+
	//			  "where stp_strtp = st_strtp and stp_matcd = st_matcd and stp_strtp = '01' and isnull(stp_ycsqt,0) >0 and isnull(stp_catfl,'') in('2','3','4') AND STP_YCSVL >= 50000 order by stp_catfl,stp_ycsvl desc,stp_matcd ";
	   
    	 	M_strSQLQRY = "select stp_matcd,stp_uomcd,stp_matds,STP_YCSQT,stp_YCSVL,STP_CATFL,stp_ownby,st_ppovl,st_pgrdt from mm_stprc,mm_stmst "+
				  "where stp_cmpcd=st_cmpcd and stp_strtp = st_strtp and stp_matcd = st_matcd and stp_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND stp_strtp = '01' and isnull(stp_ycsqt,0) >0 and isnull(stp_catfl,'') in('2','3','4') AND STP_YCSVL >= 50000 and (days(current_date) - days(ST_PISDT)) >= 365 order by stp_catfl,stp_ycsvl desc,stp_matcd ";
	   
			if(cmbRPTNM.getSelectedIndex() ==14)
			M_strSQLQRY = "select st_matcd,st_matds,st_uomcd,st_stkqt,gr_grnno,gr_batno,gr_acpqt,gr_acpqt - isnull(gr_issqt,0)l_balqt from mm_stmst,mm_grmst where " 
					  +" ST_CMPCD = GR_CMPCD and st_strtp = gr_strtp and st_matcd = gr_matcd and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND st_strtp ='08' and st_stkqt >0 order by st_matcd,gr_grnno desc";

			strSUBGR ="";
			String L_strDPTCD = "",L_strPRDPT ="";
			//M_intLINNO =0;
			M_intPAGNO =1;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
			      L_strPCATFL ="";
				String L_strPMATCD ="";
				while(M_rstRSSET.next())
				{
					if(M_intLINNO >66)
					{
						D_OUT.writeBytes("\n");
						if(cmbRPTNM.getSelectedIndex() ==0)
						    D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------");
						else if(cmbRPTNM.getSelectedIndex() ==13)
						    D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------");
						else
						    D_OUT.writeBytes("-----------------------------------------------------------------------------------------------------");
						M_intLINNO = 0;
						M_intPAGNO += 1;
						prnFMTCHR(D_OUT,M_strEJT);
						getRPHDR();
					}					
								
					L_strTEMP ="";
					
					if(cmbRPTNM.getSelectedIndex() ==0)
					{
						L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("STP_OWNBY"),"");
						if(L_intMATCT == 0)
						{
						    D_OUT.writeBytes("\n");
							D_OUT.writeBytes("Owner : "+cl_dat.getPRMCOD("CMT_CODDS","SYS","COXXDPT",L_strDPTCD));
							D_OUT.writeBytes("\n\n");
							M_intLINNO += 3;
							L_strPRDPT =L_strDPTCD;
						}
						if(!L_strPRDPT.equals(L_strDPTCD))
						{
							L_strPRDPT = L_strDPTCD;
							D_OUT.writeBytes("\n");
							D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------");
							M_intLINNO = 0;
							M_intPAGNO += 1;
							prnFMTCHR(D_OUT,M_strEJT);
							getRPHDR();
							D_OUT.writeBytes("\n");
							D_OUT.writeBytes("Owner : "+cl_dat.getPRMCOD("CMT_CODDS","SYS","COXXDPT",L_strDPTCD));
							D_OUT.writeBytes("\n\n");
							M_intLINNO += 3;
						}
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),11));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),4));
						//D_OUT.writeBytes(padSTRING('R',L_strDPTCD,4));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),50));
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),"");
						if(!L_strTEMP.equals("Y"))
							L_strTEMP ="-";
						D_OUT.writeBytes(padSTRING('R',L_strTEMP+"/",2));
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("ST_VEDFL"),"");
						if(!L_strTEMP.equals("E"))
							L_strTEMP ="-";
						D_OUT.writeBytes(padSTRING('R',L_strTEMP+"/",2));
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("ST_STSFL"),"");
						if(!L_strTEMP.equals("9"))
							L_strTEMP ="-";
						D_OUT.writeBytes(padSTRING('R',L_strTEMP,4));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),""),9));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_MAXLV"),""),9));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_SRPQT"),""),9));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_PCOQT"),"-"),9));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_CONQT"),"-"),9));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_ISSQT"),"-"),9));
					}
					else if(cmbRPTNM.getSelectedIndex() ==1)
					{
						L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("STP_OWNBY"),"");
						if(L_intMATCT == 0)
							L_strPRDPT =L_strDPTCD;
						if(!L_strPRDPT.equals(L_strDPTCD))
						{
							L_strPRDPT = L_strDPTCD;
							D_OUT.writeBytes("\n");
							D_OUT.writeBytes("-----------------------------------------------------------------------------------------------------");
							M_intLINNO = 0;
							M_intPAGNO += 1;
							prnFMTCHR(D_OUT,M_strEJT);
							getRPHDR();
							
						}
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),11));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),4));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_OWNBY"),""),4));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),52));
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),"");
						if(!L_strTEMP.equals("Y"))
							L_strTEMP ="-";
						D_OUT.writeBytes(padSTRING('R',L_strTEMP+"/",2));
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("ST_VEDFL"),"");
						if(!L_strTEMP.equals("E"))
							L_strTEMP ="-";
						D_OUT.writeBytes(padSTRING('R',L_strTEMP+"/",2));
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("ST_STSFL"),"");
						if(!L_strTEMP.equals("9"))
							L_strTEMP ="-";
						D_OUT.writeBytes(padSTRING('R',L_strTEMP,4));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),""),9));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_ISSQT"),"-"),9));
					}
					else if(cmbRPTNM.getSelectedIndex() ==2)
					{
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATCD"),""),11));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_UOMCD"),""),4));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATDS"),""),50));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_YCSQT"),""),10));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_YCSVL"),""),15));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_OWNBY"),"-"),5));
					}
					else if(cmbRPTNM.getSelectedIndex() ==3)
					{
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),"").substring(0,6),12));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),60));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_OWNBY"),""),13));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),"-")+"/"+nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),"-"),5));
						D_OUT.writeBytes(padSTRING('R',"",5));
						D_OUT.writeBytes(nvlSTRVL(M_rstRSSET.getString("CT_IILTM"),"-")+"/"+nvlSTRVL(M_rstRSSET.getString("CT_IELTM"),"-"));
					}
					else if(cmbRPTNM.getSelectedIndex() ==4)
					{
						if(!strSUBGR.equals(nvlSTRVL(M_rstRSSET.getString("MATCD"),"")))
						{
							D_OUT.writeBytes("\n");	M_intLINNO++;
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("MATCD"),""),12));
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_OWNBY"),""),12));
							strSUBGR = nvlSTRVL(M_rstRSSET.getString("MATCD"),"");
							L_intGRPCT++;
						}
						else
							D_OUT.writeBytes(padSTRING('R',"",24));
					}
					else if(cmbRPTNM.getSelectedIndex() ==5)
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CT_OWNBY"),"");
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),20));
						//D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"-"),5));
						D_OUT.writeBytes(padSTRING('R',L_strTEMP,6));
						if(L_strTEMP.length() >0)
						D_OUT.writeBytes(hstDPTCD.get(L_strTEMP).toString());
					}
					else if(cmbRPTNM.getSelectedIndex() ==6)
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CT_OWNBY"),"");
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),12));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),50));
						//D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"-"),5));
						D_OUT.writeBytes(padSTRING('R',L_strTEMP,6));
						if(L_strTEMP.length() >0)
						D_OUT.writeBytes(hstDPTCD.get(L_strTEMP).toString());
					}
					else if(cmbRPTNM.getSelectedIndex() ==7)
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CT_OWNBY"),"");
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),12));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),50));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"-"),5));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),"-"),5));
						D_OUT.writeBytes(padSTRING('R',L_strTEMP,6));
						if(L_strTEMP.length() >0)
						D_OUT.writeBytes(hstDPTCD.get(L_strTEMP).toString());
					}
					else if(cmbRPTNM.getSelectedIndex() ==8)
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CT_OWNBY")," ");
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),12));
					//	D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),50));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"-"),5));
					//	D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),"-"),5));
						D_OUT.writeBytes(padSTRING('R',L_strTEMP,6));
						if(L_strTEMP.length() >0)
						if(hstDPTCD.containsKey((String)L_strTEMP))	
						{
							D_OUT.writeBytes(padSTRING('R',hstDPTCD.get(L_strTEMP).toString(),30));
						}
						else 
							D_OUT.writeBytes(padSTRING('R'," ",30));
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CT_STSFL"),"-");
						D_OUT.writeBytes(padSTRING('R',L_strTEMP,5));
						if(!L_strTEMP.equals("9"))
						{
							getPRCDT(nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""));
							System.out.println("end "+M_rstRSSET.getString("CT_MATCD"));
						}
					}
					else if((cmbRPTNM.getSelectedIndex() ==9)||(cmbRPTNM.getSelectedIndex() ==12))
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CT_OWNBY"),"");
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),12));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),5));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),60));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_STSFL"),"-"),4));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_OWNBY"),""),5));
					
					}
					else if(cmbRPTNM.getSelectedIndex() ==10)
					{
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),12));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),5));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),50));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),""),5));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_PRTNO"),""),18));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_PPORT"),"-"),10));
						//D_OUT.writeBytes(nvlSTRVL(M_rstRSSET.getString("CT_STSFL"),"-"));
					
					}
					else if(cmbRPTNM.getSelectedIndex() ==11)
					{
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),11));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),4));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),60));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),""),12));
					}
					else if(cmbRPTNM.getSelectedIndex() ==14)
					{
						if(!M_rstRSSET.getString("ST_MATCD").equals(L_strPMATCD))
						{
							D_OUT.writeBytes("\n");
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),11));
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),4));
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),60));
							D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),""),12));
							L_strPMATCD =  M_rstRSSET.getString("ST_MATCD");
							D_OUT.writeBytes("\n");
							M_intLINNO += 2;
						}
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),10));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_BATNO"),""),13));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("gr_ACPQT"),""),10));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("L_BALQT"),""),11));
						L_dblTOTQT += M_rstRSSET.getDouble("L_BALQT");
						/*if(L_dblTOTQT > M_rstRSSET.getDouble("ST_STKQT"))
						{
							System.out.println(M_rstRSSET.getString("ST_MATCD") +" " +L_strPMATCD +" "+L_dblTOTQT);
							while(M_rstRSSET.getString("ST_MATCD").equals(L_strPMATCD))
							{
								if(M_rstRSSET.next())
									M_rstRSSET.next();
							}
						}*/
					}
					else if(cmbRPTNM.getSelectedIndex() ==13)
					{						
					    L_strCATFL = nvlSTRVL(M_rstRSSET.getString("STP_catfl"),"");
					    L_dblITVAL =M_rstRSSET.getDouble("STP_YCSVL");
					    if(!L_strCATFL.equals(L_strPCATFL))
					    {
					       if(L_intMATCT >0)
					       {
                                D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------");
				                D_OUT.writeBytes("\n");
					            M_intLINNO++;
                                prnFMTCHR(D_OUT,M_strBOLD);
					           // D_OUT.writeBytes(padSTRING('R',"Total ",80) +setNumberFormat(L_dblTOTAL,2)+"\n");
					           D_OUT.writeBytes(padSTRING('R',"Total ",73) +padSTRING('L',setNumberFormat(L_dblTOTAL,2),10)+"\n");
					            prnFMTCHR(D_OUT,M_strNOBOLD);
					            M_intLINNO++;
                      			D_OUT.writeBytes("\n");
                      			M_intLINNO++;
        						M_intLINNO = 0;
        						M_intPAGNO += 1;
        						prnFMTCHR(D_OUT,M_strEJT);
        						getRPHDR();
        		          }
				
					       prnFMTCHR(D_OUT,M_strBOLD);
					        if(L_strCATFL.equals("2"))
					            D_OUT.writeBytes("Essential Items ");
					        else if(L_strCATFL.equals("3"))
					            D_OUT.writeBytes("Obsolete Items ");
					        else if(L_strCATFL.equals("4"))
					            D_OUT.writeBytes("Desirable Items ");
					        if( L_dblITVAL >= 100000)
					        {
					            D_OUT.writeBytes("A Category ");
					            strPITCAT ="A";
					        }
					        else
					        {
					            D_OUT.writeBytes("B Category ");
					            strPITCAT ="B";
					        }
					        prnFMTCHR(D_OUT,M_strNOBOLD);
					        D_OUT.writeBytes("\n\n");    
					        M_intLINNO +=2;
                            if(M_intLINNO >66)
        					{
        						D_OUT.writeBytes("\n");
        						M_intLINNO = 0;
        						M_intPAGNO += 1;
        						prnFMTCHR(D_OUT,M_strEJT);
        						getRPHDR();
        					}		
					        L_dblTOTAL =0;
					        L_strPCATFL = L_strCATFL;    
					    }
					    else
					    {
					        if(L_dblITVAL > 100000)
					            strITCAT ="A";
					        else
					            strITCAT ="B";
   					       if(!strITCAT.equals(strPITCAT))
    					   {
    					        prnFMTCHR(D_OUT,M_strBOLD);
    					        if(L_intMATCT >0)
    					       {
    					            D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------");
				                    D_OUT.writeBytes("\n");
					                M_intLINNO++;
    					            prnFMTCHR(D_OUT,M_strBOLD);
    					          //  D_OUT.writeBytes(padSTRING('R',"Total ",80) +setNumberFormat(L_dblTOTAL,2)+"\n");
                                   D_OUT.writeBytes(padSTRING('R',"Total ",73) +padSTRING('L',setNumberFormat(L_dblTOTAL,2),10)+"\n");
                                    prnFMTCHR(D_OUT,M_strNOBOLD);
    					            M_intLINNO++;
                            		D_OUT.writeBytes("\n");
                					M_intLINNO = 0;
                					M_intPAGNO += 1;
                					prnFMTCHR(D_OUT,M_strEJT);
                					getRPHDR();
                	           }
                                if(L_strCATFL.equals("2"))
    					            D_OUT.writeBytes("Essential Items ");
    					        else if(L_strCATFL.equals("3"))
    					            D_OUT.writeBytes("Obsolete Items ");
    					        else if(L_strCATFL.equals("4"))
    					            D_OUT.writeBytes("Desirable Items ");
    					        if(L_dblITVAL >= 100000)
    					            D_OUT.writeBytes("A Category ");
    					        else
    					            D_OUT.writeBytes("B Category ");
    					        prnFMTCHR(D_OUT,M_strNOBOLD);    
    					         D_OUT.writeBytes("\n\n");    
    					        M_intLINNO += 2; 
                                if(M_intLINNO >66)
            					{
            						D_OUT.writeBytes("\n");
            						M_intLINNO = 0;
            						M_intPAGNO += 1;
            						prnFMTCHR(D_OUT,M_strEJT);
            						getRPHDR();
            					}		
    					        L_dblTOTAL =0;
    					        strPITCAT = strITCAT;
    					   }
					    }
					   	D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATCD"),""),11));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_UOMCD"),""),4));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_OWNBY"),""),4));
						D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATDS"),""),45));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_YCSQT"),""),12));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_YCSVL"),""),10));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_PPOVL"),"-"),12));
						D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_PGRDT"),""),10));
					//	D_OUT.writeInt(M_intLINNO);
                     //   D_OUT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_catfl"),""),12));
                        L_dblTOTAL +=L_dblITVAL;
					}
					D_OUT.writeBytes("\n");
					M_intLINNO++;
					L_intMATCT++;
				}
			    D_OUT.writeBytes("\n");
                if(cmbRPTNM.getSelectedIndex() ==13)
                {                
                     D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------");
				     D_OUT.writeBytes("\n");
					 M_intLINNO++;
                    prnFMTCHR(D_OUT,M_strBOLD);
        			D_OUT.writeBytes(padSTRING('R',"Total ",73) +padSTRING('L',setNumberFormat(L_dblTOTAL,2),10)+"\n");
        			prnFMTCHR(D_OUT,M_strNOBOLD);
                    prnFMTCHR(D_OUT,M_strEJT);
                }
                    if(cmbRPTNM.getSelectedIndex() ==0)
				        D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------");
				    else if(cmbRPTNM.getSelectedIndex() ==13)
				        D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------");
				    else
				        D_OUT.writeBytes("-----------------------------------------------------------------------------------------------------");
				D_OUT.writeBytes("\n");
				if(cmbRPTNM.getSelectedIndex() !=13)
				D_OUT.writeBytes(padSTRING('R',"Total Item Codes : "+L_intMATCT,32));				
			//	D_OUT.writeBytes(padSTRING('R',"Total Sub Sub group Code : "+L_intMATCT,32));				
				if(cmbRPTNM.getSelectedIndex() ==4)
					D_OUT.writeBytes(padSTRING('R',"Total Group Code  : "+L_intGRPCT,32));
				L_intMATCT = 0;
				L_intGRPCT = 0;
				strSUBGR = "";
				prnFMTCHR(D_OUT,M_strNOCPI17);
				D_OUT.close();
				F_OUT.close();
				M_intPAGNO = 1;
				M_intLINNO = 0;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		setMSG("",'N');
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	public void getRPHDR()
	{
		try
		{
		
		//	D_OUT.writeBytes(padSTRING('R',"List Of Material Code With Sub Group/Sub Sub Group Code as '00' ",76));
		//   D_OUT.writeBytes(padSTRING('R',"List Of Surplus Items with Surplus quantity > Stock on hand ",76));	
		 //   D_OUT.writeBytes(padSTRING('R',"List Of Surplus Items with Surplus quantity <= Stock on hand ",76));	
		if(cmbRPTNM.getSelectedIndex() ==0)
			D_OUT.writeBytes(padSTRING('R',"Consumption Of surplus items where stock on hand >0 ",76));
		else if(cmbRPTNM.getSelectedIndex() ==1)
			D_OUT.writeBytes(padSTRING('R',"Consumption Of Items marked as Essential ",76));
		else if(cmbRPTNM.getSelectedIndex() ==2)
			D_OUT.writeBytes(padSTRING('R',"Items in Others Category ( Descending order of Value)",76));
		else if(cmbRPTNM.getSelectedIndex() ==3)
			D_OUT.writeBytes(padSTRING('R',"List Of Sub sub Group Code with lead time as 0 ",76));			
		else if(cmbRPTNM.getSelectedIndex() ==4)
			D_OUT.writeBytes(padSTRING('R',"List Of Material Code With Sub Group Code Not Present",76));	
		else if(cmbRPTNM.getSelectedIndex() ==5)
			D_OUT.writeBytes(padSTRING('R',"List Of Header Codes With Blank Description",76));	
		else if(cmbRPTNM.getSelectedIndex() ==6)
			D_OUT.writeBytes(padSTRING('R',"List Of Item Codes With Blank UOM Code",76));	
		else if(cmbRPTNM.getSelectedIndex() ==7)
			D_OUT.writeBytes(padSTRING('R',"List Of Item Codes With Different UOM Code in Catalog and Stock ",76));	
		else if(cmbRPTNM.getSelectedIndex() ==8)
			D_OUT.writeBytes(padSTRING('R',"List Of Item Codes With Blank Description ",76));	
		else if(cmbRPTNM.getSelectedIndex() ==10)
			D_OUT.writeBytes(padSTRING('R',"List Of Item Codes with Last P.O. rate ",76));
		else if(cmbRPTNM.getSelectedIndex() ==11)
			D_OUT.writeBytes(padSTRING('R',"List Of Item Codes where stock >0 and Prv. P.O value =0 ",76));
		else if(cmbRPTNM.getSelectedIndex() ==12)
			D_OUT.writeBytes(padSTRING('R',"List Of Item Codes with blank Owner code and Owners in Main Group 76",76));
		else if(cmbRPTNM.getSelectedIndex() ==14)
			D_OUT.writeBytes(padSTRING('R',"List Of GRIN with Stock and Batch No. ",76));

		else if(cmbRPTNM.getSelectedIndex() ==13)
		//	D_OUT.writeBytes(padSTRING('R',"Category wise List of Item Codes with inventory value ",76));
			D_OUT.writeBytes(padSTRING('R',"Category wise List of Item Codes not moved for 12 months ",76));
		if(cmbRPTNM.getSelectedIndex() ==0)
		    D_OUT.writeBytes(padSTRING('R',"",32));
			D_OUT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
			D_OUT.writeBytes("\n");
			if(cmbRPTNM.getSelectedIndex() ==0)
			    D_OUT.writeBytes(padSTRING('R',"",108));
			else
			    D_OUT.writeBytes(padSTRING('R',"",76));
		    D_OUT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
		    D_OUT.writeBytes("\n");
		    if(cmbRPTNM.getSelectedIndex() ==0)
			    D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------");
			 else if(cmbRPTNM.getSelectedIndex() ==13)
			    D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------");
			else
			    D_OUT.writeBytes("-----------------------------------------------------------------------------------------------------");
			D_OUT.writeBytes("\n");
		//	D_OUT.writeBytes("Sub  Gr     Department  Material Code And Description                                          UOM");
		M_intLINNO =3;
		if(cmbRPTNM.getSelectedIndex() ==0)
			D_OUT.writeBytes("Item Code UOM and Description                                   S/E/O        Stock  Max.Lvl  Surplus   Cons.1   Cons.2   Issued");
		else if(cmbRPTNM.getSelectedIndex() ==1)
			D_OUT.writeBytes("Item Code UOM Owner and Description                                    S/E/O      Stock    Issued");
		else if(cmbRPTNM.getSelectedIndex() ==2)
			D_OUT.writeBytes("Item Code UOM Description                                         Stock Qty     Item Value  Owner");
		else if(cmbRPTNM.getSelectedIndex() ==3)
		{
			D_OUT.writeBytes("Sub sub group and Description                                               Lead Tm. Indigenous/Imported");
			D_OUT.writeBytes("\n");
			M_intLINNO +=1;
			D_OUT.writeBytes("                                                                       Owner       Int/Ext     Int./Ext");
		}
		else if(cmbRPTNM.getSelectedIndex() ==5)
			D_OUT.writeBytes("Item Code           Owner Description");
		else if(cmbRPTNM.getSelectedIndex() ==6)
			D_OUT.writeBytes("Item Code   Item Description                                  Owner Description");
		else if(cmbRPTNM.getSelectedIndex() ==7)
			D_OUT.writeBytes("Item Code   Item Description                                  CT-UOM  ST-UOM Owner Description");
		else if(cmbRPTNM.getSelectedIndex() ==8)
			D_OUT.writeBytes("Item Code   UOM  Owner Description                   Status");
		else if((cmbRPTNM.getSelectedIndex() ==9)||(cmbRPTNM.getSelectedIndex() ==12))
			D_OUT.writeBytes("Item Code   UOM  Item Description                                       Status  Owner");
		else if(cmbRPTNM.getSelectedIndex() ==10)
			D_OUT.writeBytes("Item Code   UOM  Item Description                               Level   Part Number             Rate");
		else if(cmbRPTNM.getSelectedIndex() ==11)
			D_OUT.writeBytes("Item Code UOM Description                                                     Stock Qty     ");
		else if(cmbRPTNM.getSelectedIndex() ==13)
			D_OUT.writeBytes("Item Code UOM Dpt Description                                      Stock Qty   Value   Prv.P.O.val Prv.Rct.Dt.");
		else if(cmbRPTNM.getSelectedIndex() ==14)
		{	
			D_OUT.writeBytes("Item Code UOM Description                                                     Stock Qty     ");
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes("GRIN NO   Batch No.      Acp. Qty   Bal. Qty ");
			M_intLINNO +=1;

		}
			
			D_OUT.writeBytes("\n");
			if(cmbRPTNM.getSelectedIndex() ==0)
			    D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------");
			else if(cmbRPTNM.getSelectedIndex() ==13)
			    D_OUT.writeBytes("--------------------------------------------------------------------------------------------------------------------");
			else
			    D_OUT.writeBytes("-----------------------------------------------------------------------------------------------------");
			D_OUT.writeBytes("\n");
			M_intLINNO +=2;
		}
		
		catch(IOException L_IO)
		{
			setMSG(L_IO,"getRPHDR");
		}
	}
	private void getPRCDT(String P_strMATCD)
	{
		try
		{
			ResultSet L_rstRSSET;
			setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strLVLNO ="";
			String L_strMATCD = P_strMATCD;//TCD.getText().trim();//tblINDTL.getValueAt(tblINDTL.getSelectedRow(),TBL_MATCD).toString();
			M_strSQLQRY = "SELECT CT_LVLRF from CO_CTMST where ct_codtp ='CD' and ct_matcd ='"+L_strMATCD +"'";
			M_strSQLQRY += " AND isnull(ct_stsfl,'') <>'X'";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
					L_strLVLNO = nvlSTRVL(L_rstRSSET.getString("CT_LVLRF"),"");
				L_rstRSSET.close();
			}
			M_strSQLQRY = "Select CTT_MATDS,CTT_MATCD,CTT_LVLNO,ctt_linno from CO_CTTRN ";
			 M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='MG' ";
			 M_strSQLQRY += " and ctt_matcd = '"+L_strMATCD.substring(0,2) +"0000000A'";
			 M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			 M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
			 M_strSQLQRY += "UNION ";
			 M_strSQLQRY += "Select CTT_MATDS,CTT_MATCD,CTT_LVLNO,ctt_linno from CO_CTTRN ";
			 M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SG' ";
			 M_strSQLQRY += " and ctt_matcd = '"+L_strMATCD.substring(0,4) +"00000A'";
			 M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			 M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
						
			 M_strSQLQRY += "UNION ";
			 M_strSQLQRY += "Select CTT_MATDS,CTT_MATCD,CTT_LVLNO,ctt_linno from CO_CTTRN ";
			 M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SS' ";
			 M_strSQLQRY += " and ctt_matcd = '"+L_strMATCD.substring(0,6) +"000A'";
			 M_strSQLQRY += " and CTT_LVLNO ='00'";
			 M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			 M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
						
			/* if(!L_strLVLNO.equals("00"))
			{
			 	M_strSQLQRY += "UNION ";
			 	M_strSQLQRY += "Select CTT_MATDS,CTT_MATCD,CTT_LVLNO,ctt_linno from CO_CTTRN ";
			 	M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SS' ";
			 	M_strSQLQRY += " and ctt_matcd = '"+L_strMATCD.substring(0,6) +"000A'";
			 	M_strSQLQRY += " and CTT_LVLNO ='"+L_strLVLNO+"'";
			 	M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			 	M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
			 }
			 M_strSQLQRY += "UNION ";
			 M_strSQLQRY += "Select CTT_MATDS,CTT_MATCD,CTT_LVLNO,ctt_linno from CO_CTTRN ";
			 M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='CD' ";
			 M_strSQLQRY += " and ctt_matcd = '"+L_strMATCD +"'";
			 M_strSQLQRY += " and CTT_LVLNO ='"+L_strLVLNO+"'";
			 M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			 M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
			 M_strSQLQRY += " Order by ctt_matcd,ctt_lvlno,ctt_linno ";*/
			 L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			 int L_intROWCT =0;
			 if(L_rstRSSET != null)	
			 {
				while(L_rstRSSET.next())
				{
					 System.out.println(L_strMATCD +" "+nvlSTRVL(L_rstRSSET.getString("CTT_LINNO"),"")+""+nvlSTRVL(L_rstRSSET.getString("CTT_MATDS"),""));
					 D_OUT.writeBytes("\n "+padSTRING('L',"",8));
					 D_OUT.writeBytes(nvlSTRVL(L_rstRSSET.getString("CTT_LINNO"),"")+" "+nvlSTRVL(L_rstRSSET.getString("CTT_MATDS"),""));
				}
				L_rstRSSET.close();
			 }
		}
		catch(Exception L_E)
		{
			System.out.println(L_E.toString());
			setMSG(L_E,"PRC DET.");
		}
		finally
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	public void exePRINT()
	{
	    //updTXDOC();
		getDATA();
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			doPRINT(strFILNM);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
		{
			try
			{
				Runtime r = Runtime.getRuntime();
				Process p = null;
				p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM); 
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"Error.exescrn.. ");
			}
		}
	}
	private void updTXDOC()
	{
	    String L_strSYSCD="";
	    String L_strSBSTP="";
	    String L_strDOCTP="";
	    String L_strDOCNO="";
	    String L_strPRDCD="";
	    String L_strAMDNO="";
	    int L_ERRCT =0,L_SAVCT =0,L_ERRSV =0,L_INTCT=0;
	    try
	    {
            M_strSQLQRY ="SELECT * from CO_TXDAM WHERE TX_SYSCD ='MM'";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
            if(M_rstRSSET !=null)
            {
                while(M_rstRSSET.next())
                {
                   try
                   {
                        L_INTCT++;
                        L_strSYSCD = M_rstRSSET.getString("TX_SYSCD");
                        L_strSBSTP = M_rstRSSET.getString("TX_SBSTP");
                        L_strDOCTP = M_rstRSSET.getString("TX_DOCTP");
                        L_strDOCNO = M_rstRSSET.getString("TX_DOCNO");
                        L_strPRDCD = M_rstRSSET.getString("TX_PRDCD");
                        L_strAMDNO = M_rstRSSET.getString("TX_AMDNO");
                        M_strSQLQRY ="INSERT INTO TT_TXDAM1 select * from co_txDAM WHERE ";
                        M_strSQLQRY +=" TX_SYSCD ='"+L_strSYSCD+"'";
                        M_strSQLQRY +=" AND TX_SBSTP ='"+L_strSBSTP+"'";
                        M_strSQLQRY +=" AND TX_DOCTP ='"+L_strDOCTP+"'";
                        M_strSQLQRY +=" AND TX_DOCNO ='"+L_strDOCNO+"'";
                        M_strSQLQRY +=" AND TX_PRDCD ='"+L_strPRDCD+"'";
                        L_STAT.executeUpdate(M_strSQLQRY);
                        ///cl_dat.exeSQLUPD(M_strSQLQRY,"");
                        cl_dat.exeDBCMT("updTXDOC");
                        L_SAVCT++;
                        setMSG(L_INTCT+" SAVED :"+L_SAVCT +" NOT SAVED "+L_ERRCT,'N');
                   }
                   catch(Exception L_E1)
            	    {
            	       try
            	       {
            	        L_ERRCT++;
            	         System.out.println("executing 2");
            	         M_strSQLQRY ="INSERT INTO TT_TXDAM2 select * from co_txDAM WHERE ";
                                M_strSQLQRY +=" TX_SYSCD ='"+L_strSYSCD+"'";
                                M_strSQLQRY +=" AND TX_SBSTP ='"+L_strSBSTP+"'";
                                M_strSQLQRY +=" AND TX_DOCTP ='"+L_strDOCTP+"'";
                                M_strSQLQRY +=" AND TX_DOCNO ='"+L_strDOCNO+"'";
                                M_strSQLQRY +=" AND TX_PRDCD ='"+L_strPRDCD+"'";
                         L_STAT.executeUpdate(M_strSQLQRY);
                         cl_dat.exeDBCMT("updTXDOC");
                         L_ERRSV++;
            	       }
            	       catch(Exception L_E)
            	       {
            	            System.out.println("ERROR1");
            	       }
            	    }
                }
            }
	    }
	    catch(Exception L_E1)
	    {
	        System.out.println("Error");
	    }
	    System.out.println("L_INTCT : "+L_INTCT+"L_SAVCT : "+L_SAVCT +" L_ERRCT : "+L_ERRCT +" L_ERRSV : "+L_ERRSV);
	}
}