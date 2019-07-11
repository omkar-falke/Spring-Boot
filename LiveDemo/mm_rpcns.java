/*
System Name   : Material Management System
Program Name  : Consignment wise Reciept.
Program Desc. : Report for Consignment wise receipt
Author        : Mr S.R.Mehesare
Date          : 25.02.2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;
/**<pre>
System Name : Material Management System.
 
Program Name : Consignment wise Receipts

Purpose : This program generates Report on Material Receipts for a given Consignment number.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_BETRN       BE_STRTP,BE_PORNO,BE_CONNO,BE_BOENO                     #	     
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
CO_CTMST       CT_MATCD                                                #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtCONNO    BE_CONNO       MM_BETRN      Varchar(15)   Consignment Number
txtCONDT    BE_CONDT       MM_BETRN      Date          Date
txtCONDS    BE_CONDS       MM_BETRN      Varchar(100)  Description
--------------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b> 1)MM_BETRN
                        2)CO_CDTRN                       
                        3)CO_CTMST
<B>Conditions Give in Query:</b>
    Data is taken from MM_BETRN,MM_GRMST with CO_CTMST for the given Consignment Number
        1)GR_MATCD = CT_MATCD
        2)and GR_CNSNO = BE_CONNO   
        3)and GR_BOENO = BE_BOENO
        4)and GR_STRTP = Selected Stores (Tankfarm)
        5)and GR_CNSNO = given Consignment Number             			    

<B>Validations & Other Information:</B>
    - Consignment Number must be present in the DataBase.
    - Location Name & Excise Category are taken from CO_CDTRN for correspnding Codes as,
         For Location Name Condition is CMT_CGMTP = 'SYS' and CMT_CGSTP ='QC11TKL'.
         For Excise Category Condition is CMT_CGMTP = 'SYS' and CMT_CGSTP ='MMXXMAT'.
</I> */


public class mm_rpcns extends cl_rbase
{  									/** JTextField to accept & display Consignment Number.*/
	private JTextField txtCONNO;	/** JTextField to display Consignment Date.*/
	private JTextField txtCONDT;	/** JTextField to display Consignment Description.*/
	private JTextField txtCONDS;	/** String for generated Report File Name*/	                     
	private String strFILNM;		/** Integer for counting the records fetched from DB.*/	                     
	private int intRECCT=0;			/** String for Store Type Code.*/	                     
	private String strSTRTP;		/** String for Consignment Number.*/	                     
	private String strCONNO;		/** String for Consignment description.*/	                     
	private String strCONDS;		/** String for Consignment Quantity.*/	                     
	private String strCONQT;		/** String for Unit of Measurement.*/	                     
	private String strUOMCD;		/** String for Consignment Date.*/	                     
	private String strCONDT;		/** String for Material Description.*/	                     
	private String strMATDS;		/** Float for total Challan Quantity.*/	                     
	private float fltTCHLQT;		/** Float for total Reciept Quantity.*/	                     
	private float fltTRECQT;		/** File Output Stream for File Handleing.*/
	private FileOutputStream fosREPORT;	/** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;	/** ResultSet Object for Database I/O Functions.*/
    private ResultSet M_rstRSSET1;   	/** Hash Table to hold Excise Category Code, Description & Location Code, Description.*/
	private Hashtable<String,String> hstCODDS;
	mm_rpcns()
	{
	    super(2);
	    try
	    {			
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("Consignment No."),4,4,1,2.5,this,'L');
			add(txtCONNO= new JTextField(),4,5,1,1,this,'L');			
			add(new JLabel("Date"),5,4,1,2.5,this,'L');
			add(txtCONDT= new TxtDate(),5,5,1,1,this,'L');			
			add(new JLabel("Description."),6,4,1,2.5,this,'L');
			add(txtCONDS= new JTextField(),6,5,1,3,this,'L');			
			M_pnlRPFMT.setVisible(true);			
			hstCODDS = new Hashtable<String,String>();
			setCursor(cl_dat.M_curWTSTS_pbst);	
			M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP IN('MMXXMAT','QC11TKL')";
			M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
            if(M_rstRSSET != null)    			        		
		    {				
        		while(M_rstRSSET.next())
        		{        		                 			
					hstCODDS.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
        		}
        		M_rstRSSET.close();
            } 					
			setCursor(cl_dat.M_curDFSTS_pbst);	
	 		setENBL(true);
	    }
	    catch(Exception L_EX)
		{
			setMSG(L_EX+" GUI Designing",'E');
		}	
    }
	/**
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		txtCONNO.setEnabled(L_flgSTAT);
		txtCONDT.setEnabled(false);
		txtCONDS.setEnabled(false);		
		if (L_flgSTAT==false)
		{			
			txtCONNO.setText("");
			txtCONDT.setText("");			    
			txtCONDS.setText("");			    			
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{	 
	    super.actionPerformed(L_AE); 
	    if(M_objSOURC == txtCONNO)
		{	
			setCursor(cl_dat.M_curWTSTS_pbst);
			if((txtCONNO.getText().trim()).length()<=8)
			{	
				txtCONDT.setText("");
				txtCONDS.setText("");
				try
				{
					M_strSQLQRY = "Select BE_CONDS,BE_CONDT from MM_BETRN where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_CONNO = '" + txtCONNO.getText().trim() +"'"; 						
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);									
					if(M_rstRSSET != null)    			        		
					{
						if (M_rstRSSET.next())
						{
							java.sql.Date tempDATE = M_rstRSSET.getDate("BE_CONDT");
							if (tempDATE!= null)
								txtCONDT.setText(M_fmtLCDAT.format(tempDATE));											
							txtCONDS.setText(M_rstRSSET.getString("BE_CONDS"));
						}
						M_rstRSSET.close();
					}
					else
						setMSG("Consignment number..",'E');						
				    cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"Consignment No..");					
				}	
			}
			else
				setMSG("Enter Consignment No And Press Enter Key. OR Press F1 To Select From List..",'N');
			setCursor(cl_dat.M_curDFSTS_pbst);
		}			
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO=0;
			fltTCHLQT = 0;
			fltTRECQT = 0;				
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {
 	        setCursor(cl_dat.M_curWTSTS_pbst);
    	    try
    	  	{				    	        
    			M_strSQLQRY = "Select distinct BE_CONNO,BE_CONDS,BE_CONDT from MM_BETRN where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
				if (txtCONNO.getText().length()!=0)
					M_strSQLQRY +=	" and BE_CONNO LIKE '" +txtCONNO.getText().trim() + "%'";
				M_strSQLQRY += " order by BE_CONDT desc";				
    			M_strHLPFLD = "txtCONNO";			           						
    			cl_hlp(M_strSQLQRY,2,1,new String[]{"Consignment No.","Description","Date"},3,"CT");    			
    		}
    	    catch(Exception L_EX)
    	    {
    		    setMSG(L_EX ," F1 help..");    		    
    	    }
    	    setCursor(cl_dat.M_curDFSTS_pbst);
 	    }
	}
	/**
	Method for execution of help for Consignment Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtCONNO")
		{
			txtCONNO.setText(cl_dat.M_strHLPSTR_pbst);				
			txtCONDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());
			txtCONDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			if (txtCONNO.getText().length()<=8)			
			    cl_dat.M_btnSAVE_pbst.requestFocus();
		}		
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
			return;
	
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpcns.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpcns.doc";				
			getALLREC();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
			if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
				else 
		        {    
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			     Runtime r = Runtime.getRuntime();
				 Process p = null;					
			     if(M_rdbHTML.isSelected())
			        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
			     else
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Consignment wise Receipts"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT.. ");
		}		
	}	
    /**
    *Method to fetch data from MM_GRMST,CO_CTMST & MM_BETRN tables & club it with Header &
    *footer in Data Output Stream.
	*/
	private void getALLREC()
	{ 		
		String L_strMATCD,L_strEXCCT,L_strTRNCD,L_strTRNNM,L_strGINNO,L_strLRYNO,L_strCHLNO;
		String L_strCHLDT="",L_strCHLQT,L_strRECQT,L_strSHRQT,L_strGRNNO,L_strBOENO,L_strLOCCD,L_strMATTP="",L_strBOEQT,L_strBOEDT="",L_strLOCDS;
		String L_OMATCD = "",L_strOTRNCD = "",L_strOBOENO ="",L_strPRNSTR;		
		float L_fltTCHLQT=0,L_fltTRECQT=0,L_fltTDIFQT=0;
		float L_fltMCHLQT=0,L_fltMRECQT=0,L_fltMDIFQT=0,L_fltDIFQT=0,L_fltMBOEQT=0;		
		java.sql.Date tempDATE1;
		java.sql.Date tempDATE;
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
	    {	        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Consignment wise Reciepts </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}			
			strSTRTP=M_strSBSCD.substring(2,4);
			M_strSQLQRY = "Select GR_MATCD,CT_MATDS,CT_UOMCD,";
			M_strSQLQRY += "GR_EXCCT,GR_TRNCD,GR_TRNNM,";
			M_strSQLQRY += "GR_GINNO,GR_LRYNO,GR_CHLNO,";
			M_strSQLQRY += "GR_CHLDT,GR_CHLQT,GR_RECQT,";
			M_strSQLQRY += "GR_GRNNO,GR_CNSNO,GR_BOENO,BE_CONNO,BE_CONDT,BE_BOENO,BE_BOEDT,BE_BOEQT,BE_LOCCD,BE_CONDS,BE_CONQT";
			M_strSQLQRY += " from MM_GRMST,CO_CTMST,MM_BETRN";
			M_strSQLQRY += " where GR_MATCD = CT_MATCD ";
			M_strSQLQRY += " and GR_CMPCD = BE_CMPCD and GR_CNSNO = BE_CONNO AND GR_BOENO = BE_BOENO ";
			M_strSQLQRY += " and GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') <> 'X' and BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(BE_STSFL,'') <> 'X'";
			M_strSQLQRY += " and isnull(CT_STSFL,'') <> 'X'";
			M_strSQLQRY += " and GR_STRTP = '" + strSTRTP + "'";
			M_strSQLQRY += " and GR_CNSNO = '" + txtCONNO.getText().trim()+"'";
			M_strSQLQRY += " order by GR_BOENO,GR_TRNCD,GR_GINNO"; 						
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);					
			if(M_rstRSSET !=null)
			{						
				while(M_rstRSSET.next())
				{				
					L_strGRNNO = "";
					strCONNO = txtCONNO.getText().trim();
					L_strMATCD = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");					
					  strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
					  strUOMCD = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"");
					L_strEXCCT = nvlSTRVL(M_rstRSSET.getString("GR_EXCCT"),"");
					L_strTRNCD = nvlSTRVL(M_rstRSSET.getString("GR_TRNCD"),"-");
					L_strTRNNM = nvlSTRVL(M_rstRSSET.getString("GR_TRNNM"),"-");
					L_strGINNO = nvlSTRVL(M_rstRSSET.getString("GR_GINNO"),"");
					L_strLRYNO = nvlSTRVL(M_rstRSSET.getString("GR_LRYNO"),"");
					L_strCHLNO = nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),"");					
					tempDATE = M_rstRSSET.getDate("GR_CHLDT");
					if (tempDATE!= null)
					    L_strCHLDT = M_fmtLCDAT.format(tempDATE);											
					L_strCHLQT = nvlSTRVL(M_rstRSSET.getString("GR_CHLQT"),"0");
					L_strRECQT = nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"0");
					L_fltDIFQT = Float.valueOf(L_strRECQT).floatValue() - Float.valueOf(L_strCHLQT).floatValue();
					L_strSHRQT = setNumberFormat(L_fltDIFQT,3);
					L_strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
					L_strBOENO = nvlSTRVL(M_rstRSSET.getString("GR_BOENO"),"");
					L_strBOEQT = nvlSTRVL(M_rstRSSET.getString("BE_BOEQT"),"0");										
					L_strBOEDT = M_fmtLCDAT.format(M_rstRSSET.getDate("BE_BOEDT"));
					L_strLOCCD = nvlSTRVL(M_rstRSSET.getString("BE_LOCCD"),"");
					strCONDS = nvlSTRVL(M_rstRSSET.getString("BE_CONDS"),"");					
					tempDATE1 = M_rstRSSET.getDate("BE_CONDT");
					if (tempDATE1 != null)
						strCONDT = M_fmtLCDAT.format(tempDATE);					
					strCONQT = nvlSTRVL(M_rstRSSET.getString("BE_CONQT"),"0");
					if(intRECCT ==0)
						prnHEADER();
					if(cl_dat.M_intLINNO_pbst> 64)
					{						
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							dosREPORT.writeBytes(padSTRING('R'," ",8));
						dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------");			
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();					
					}
					if(!L_strBOENO.trim().equals(L_strOBOENO))	// Material Changes
					{						
						if(intRECCT != 0)
						{
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))				
							{
								dosREPORT.writeBytes(padSTRING('R'," ",8));
								prnFMTCHR(dosREPORT,M_strBOLD);
							}
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>");							
							dosREPORT.writeBytes(padSTRING('L',"Total : ",55));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTCHLQT,3),9));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTRECQT,3),15));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTDIFQT,3),12) + "\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))																			
								prnFMTCHR(dosREPORT,M_strNOBOLD);							
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</b>");							
							cl_dat.M_intLINNO_pbst+= 1;
							if(cl_dat.M_intLINNO_pbst> 64)
							{
								dosREPORT.writeBytes("\n");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",8));
								dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------");			
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								prnHEADER();
							}
							L_strOTRNCD = L_strTRNCD;
							L_fltTCHLQT = L_fltTRECQT = L_fltTDIFQT = 0;
							dosREPORT.writeBytes("\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",8));
							dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------");			
							fltTCHLQT += L_fltMCHLQT ;
							fltTRECQT += L_fltMRECQT ;
							dosREPORT.writeBytes("\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",8));
							dosREPORT.writeBytes(padSTRING('L',"Total : ",55) + padSTRING('L',setNumberFormat(L_fltMCHLQT,3),9)  + padSTRING('L',setNumberFormat(L_fltMRECQT,3),15)  + padSTRING('L',setNumberFormat(L_fltMDIFQT,3),12) + "\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",8));
							dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------\n");										
							cl_dat.M_intLINNO_pbst+= 3;
							if(cl_dat.M_intLINNO_pbst> 64)
							{
								dosREPORT.writeBytes("\n");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",8));
								dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------");			
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								prnHEADER();
							}
							L_fltMCHLQT = L_fltMRECQT = L_fltMDIFQT = L_fltMBOEQT =0;
						}				
						L_strOBOENO = L_strBOENO;				
						L_strOTRNCD = L_strTRNCD;									
						if(hstCODDS.containsKey(L_strEXCCT))
							L_strMATTP = hstCODDS.get(L_strEXCCT).toString();
						L_strLOCDS = hstCODDS.get(L_strLOCCD).toString();												
						dosREPORT.writeBytes("\n");												
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))						
						{
							dosREPORT.writeBytes(padSTRING('R'," ",8));
							prnFMTCHR(dosREPORT,M_strBOLD);						
						}
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");						
						dosREPORT.writeBytes(padSTRING('R',L_strBOENO,15));
						dosREPORT.writeBytes(padSTRING('R',L_strBOEDT,12));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Float.valueOf(L_strBOEQT).floatValue(),3),15));
						dosREPORT.writeBytes(padSTRING('R'," ",7)+padSTRING('R',L_strMATTP,20)+padSTRING('R',L_strLOCDS,15)+ "\n\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							dosREPORT.writeBytes(padSTRING('R'," ",8));
						dosREPORT.writeBytes(padSTRING('R',L_strTRNCD,7) + L_strTRNNM + "\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");					
						cl_dat.M_intLINNO_pbst+= 4;
						if(cl_dat.M_intLINNO_pbst> 64)
						{
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",8));
							dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------");			
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))							
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();
						}
					}
					if(!L_strTRNCD.trim().equals(L_strOTRNCD))		// Transporter Changes
					{						
						if(intRECCT != 0)
						{
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							{
								dosREPORT.writeBytes(padSTRING('R'," ",8));
								prnFMTCHR(dosREPORT,M_strBOLD);
							}
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<b>");
							dosREPORT.writeBytes(padSTRING('L',"Total : ",55));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTCHLQT,3),9));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTRECQT,3),15));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTDIFQT,3),12) + "\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))														
								prnFMTCHR(dosREPORT,M_strNOBOLD);							
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</b>");
							cl_dat.M_intLINNO_pbst+= 1;						
							if(cl_dat.M_intLINNO_pbst> 64)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									dosREPORT.writeBytes(padSTRING('R'," ",8));
								dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------");			
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								prnHEADER();
							}							
							L_fltTCHLQT = L_fltTRECQT = L_fltTDIFQT = 0;
						}
						L_strOTRNCD = L_strTRNCD;
						dosREPORT.writeBytes("\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						{
							dosREPORT.writeBytes(padSTRING('R'," ",8));
							prnFMTCHR(dosREPORT,M_strBOLD);
						}
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						dosREPORT.writeBytes(padSTRING('R',L_strTRNCD,7) + L_strTRNNM + "\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);						
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");
						cl_dat.M_intLINNO_pbst+= 2;						
						if(cl_dat.M_intLINNO_pbst> 64)
						{
							dosREPORT.writeBytes("\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",8));
							dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------");			
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();
						}
					}					
					L_fltTCHLQT += Float.valueOf(L_strCHLQT).floatValue();
					L_fltTRECQT += Float.valueOf(L_strRECQT).floatValue();					
					L_fltTDIFQT += L_fltDIFQT;				
					L_fltMCHLQT += Float.valueOf(L_strCHLQT).floatValue();
					L_fltMRECQT += Float.valueOf(L_strRECQT).floatValue();
					L_fltMBOEQT += Float.valueOf(L_strBOEQT).floatValue();				
					L_fltMDIFQT += L_fltDIFQT;					
					L_strPRNSTR = "";					
					L_strPRNSTR += padSTRING('R',L_strGINNO,14);
					L_strPRNSTR += padSTRING('R',L_strLRYNO,15);
					L_strPRNSTR += padSTRING('R',L_strCHLNO,10);
					L_strPRNSTR += padSTRING('R',L_strCHLDT,15);								
					L_strPRNSTR += padSTRING('L',setNumberFormat(Float.valueOf(L_strCHLQT).floatValue(),3),10);
					L_strPRNSTR += padSTRING('L',setNumberFormat(Float.valueOf(L_strRECQT).floatValue(),3),15);
					L_strPRNSTR += padSTRING('L',L_strSHRQT,12);
					L_strPRNSTR += "     ";
					L_strPRNSTR += padSTRING('R',L_strGRNNO,13);					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",8));
					dosREPORT.writeBytes(L_strPRNSTR + "\n");					
					cl_dat.M_intLINNO_pbst += 1;
					intRECCT++;					
				}
				M_rstRSSET.close();
			}
			else 
				setMSG("No Data Found..",'E');
			if(intRECCT == 0)
				setMSG("No Data Found for the given selection..",'E');
			else
			{
				if(cl_dat.M_intLINNO_pbst> 64)
				{
					dosREPORT.writeBytes("\n");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes( padSTRING('R'," ",8));
					dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------");			
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					prnHEADER();
				}
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<b>");
				fltTCHLQT += L_fltMCHLQT ;
				///fltTRECQT += L_fltMCHLQT ;
				fltTRECQT += L_fltMRECQT ;
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",8));
				dosREPORT.writeBytes(padSTRING('L',"Total : ",55) + padSTRING('L',setNumberFormat(L_fltTCHLQT,3),9)  + padSTRING('L',setNumberFormat(L_fltTRECQT,3),15)  + padSTRING('L',setNumberFormat(L_fltTDIFQT,3),12) + "\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------\n");			
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
				dosREPORT.writeBytes(padSTRING('L',"Total : ",55) + padSTRING('L',setNumberFormat(L_fltMCHLQT,3),9)  + padSTRING('L',setNumberFormat(L_fltMRECQT,3),15)  + padSTRING('L',setNumberFormat(L_fltMDIFQT,3),12) + "\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------");			
				String L_strTBEQT = getBOEQT(txtCONNO.getText().trim());
				String L_strBERDF = String.valueOf(Float.parseFloat(L_strTBEQT) -L_fltMRECQT);
				dosREPORT.writeBytes("\n\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",8));
				dosREPORT.writeBytes("Total Bill of entry Qty    :  "+padSTRING('L',setNumberFormat(Float.valueOf(L_strTBEQT).floatValue(),3),10)+"     Shortage qty(BOE -Receipt)    : "+padSTRING('L',setNumberFormat(Float.valueOf(L_strBERDF).floatValue(),3),12)+"\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",8));
				dosREPORT.writeBytes("Total Challan Qty          :  "+padSTRING('L',setNumberFormat(fltTCHLQT,3),10)+"     Shortage qty(Challan -Receipt): "+padSTRING('L',setNumberFormat(L_fltMDIFQT,3),12)+"\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",8));
				dosREPORT.writeBytes("Total Receipt Qty          :  "+padSTRING('L',setNumberFormat(fltTRECQT,3),10)+"\n");			
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);				
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</b>");
				setMSG("Report completed.. ",'N');
			}	
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}
	/**
	Method to validate Consignment number, before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{
		if(txtCONNO.getText().length()==0)			
		{
			setMSG("Please Enter Consignment andPress Enter Key.. ",'E');
			txtCONNO.requestFocus();			
			return false;
		}
		if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if (M_cmbDESTN.getItemCount()==0)
			{					
				setMSG("Please Select the Email/s from List through F1 Help ..",'N');
				return false;
			}
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		{ 
			if (M_cmbDESTN.getSelectedIndex() == 0)
			{	
				setMSG("Please Select the Printer from Printer List ..",'N');
				return false;
			}
		}
		return true;
	}
	/**
	 * Method to Generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;
			strSTRTP=M_strSBSCD.substring(2,4);					
			dosREPORT.writeBytes("\n\n\n\n\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,80));
			dosREPORT.writeBytes(padSTRING('L',"Report Date : " + cl_dat.M_strLOGDT_pbst,24) + "\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_cmbSBSL2_pbst.getSelectedItem() + " Consignment wise  Receipts ",80));
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes( padSTRING('R'," ",8));
			dosREPORT.writeBytes("Consignment No.  : "+padSTRING('R',strCONNO,20)+"Description : "+strCONDS+"\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
			dosREPORT.writeBytes("Consignment Date : "+padSTRING('R',strCONDT,20)+     "Cons. QTY   : "+strCONQT+"\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
			dosREPORT.writeBytes("Material Desc.   : "+padSTRING('R',strMATDS,20)+     "UOM         : "+strUOMCD + "\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
			dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
			dosREPORT.writeBytes("BOE No.        BOE Date           BOE Qty.       Category            Despatch Location \n");					
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
			dosREPORT.writeBytes("Gate-In No.  Vehicle No.    Chl. No.   Chl. Date        Chl. Qty    Receipt Qty    Shortage     GRIN No.\n");
			cl_dat.M_intLINNO_pbst += 15;
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
			dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------\n");						
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	/**
	*Method to get the Sum of Bill Od Entry Quantity for a given consignment Number.
	* @param P_strCONNO String argument to pass Consignment Number.
	*/
	private String getBOEQT(String P_strCONNO)
	{
		try
		{
			M_strSQLQRY = "Select BE_CONNO,sum(BE_BOEQT) L_BOEQT from MM_BETRN ";
			M_strSQLQRY += " where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_CONNO ='"+P_strCONNO+"'";
			M_strSQLQRY += " and isnull(BE_STSFL,'') <> 'X' group by BE_CONNO";			
			M_rstRSSET1 = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(M_rstRSSET1 !=null)
			{
				if(M_rstRSSET1.next())
				{
					return M_rstRSSET1.getString("L_BOEQT");					
				}
				M_rstRSSET1.close();
			}
		}	
		catch(Exception L_EX)
		{
			setMSG(L_EX, "getBOEQT");
		}
	return "0";
	}	
}