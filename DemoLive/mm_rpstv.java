/*
System Name		: Materials Management System
Program Name	: Owner / Category wise Stock Value Details 
Author			: Mr. S.R.Mehesare
Modified Date	: 24/10/2005
Documented Date	: 
Version			: MMS v2.0.0
*/

import javax.swing.JTextField;import javax.swing.JComboBox;
import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.util.Hashtable;import javax.swing.JLabel;import java.sql.ResultSet;
/**<pre>
System Name : Material Management System.
 
Program Name : Owner / Category wise Stock Value Details

Purpose : Program for Owner / Category wise Stock Value Details.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
CO_CTPTR       CTP_MATCD,CTP_PRTTP,CTP_PRTCD                           #
MM_STPRC       STP_MMSBS,STP_STRTP,STP_MATCD                           #
MM_STMST       ST_MMSBS,ST_STRTP,ST_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name     Column Name       Table name      Type/Size      Description
--------------------------------------------------------------------------------------
cmbDPTCD       STP_OWNBY         MM_STMST        VARCHAR(3)     Owener of the Material     
cmbITMCT       STP_CATFL         MM_STMST        VARCHAR(1)     Item Category
rdoOPRCN       STP_MATCD         MM_STMST        VARCHAR(6)     Material Code
rdoOPOTH       STP_MATCD         MM_STMST        VARCHAR(6)     Material Code
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
	Report Data is taken from MM_STPRC,CO_CTMST,MM_STMST and CO_CTPTR for condiations :-
      1) Left Outer join CO_CTPTR ON CTP_MATCD = ST_MATCD 
      2) AND isnull(CTP_LPONO,'') = isnull(ST_PPONO,'') 
      3) WHERE STP_STRTP = ST_STRTP 
      4) AND STP_MATCD = ST_MATCD 
      5) AND STP_MATCD = CT_MATCD
      6) AND isnull(STP_YOSQT,0) >0 
      7) AND STP_STRTP = selected Store Type
      8) AND isnull(STP_CATFL,'') = Specify Item Category.
      9) AND isnull(STP_OWNBY,'') = Specify Owner
    if Reconditioned Items Specified
     10) AND SUBSTR(STP_MATCD,10,1) IN('8','9')
    else if OtherItems Specified
	 11) AND SUBSTR(STP_MATCD,10,1) NOT IN('8','9')
</I> */
public class mm_rpstv extends cl_rbase
{								/** JComboBox to specify the Owner of the Materials.*/
    private JComboBox cmbDPTCD;	/** JComoboBox to Specify the Item Category.*/
	private JComboBox cmbITMCT;	/** Integer Varaible to count the number of records fetched to block the Report if no data found.*/
    private int intRECCT = 0;	/** FileOutPutStream Object to generate the Report file from the Stream of data.*/
    private FileOutputStream fosREPORT;/** DataOutPutStream Object to generate & hold the stream of Data.*/
    private DataOutputStream dosREPORT;/** Hashtable to hold the Part code & Associated Party Name.*/
    private Hashtable<String,String> hstPRTCD;		   /** JRedioButton to specify the Reconditioned Items.*/
    private JRadioButton rdoOPRCN;	   /** JRedioButton to specify the Other Items.*/
    private JRadioButton rdoOPOTH;	   /** Button group to bind the RedioButtons in a Group.*/
    private ButtonGroup bgrRPTOP;      /** String variable for generated Report File Name.*/
    private String strFILNM;		   /** String varaible to apppend the Dotted Line in the Report.*/
	private String strDOTLN = "---------------------------------------------------------------------------------------------------------------------------------------------------";
    mm_rpstv()
    {
		super(2);
        try
        {
			setMatrix(20,8);
            M_vtrSCCOMP.remove(M_lblFMDAT);
    		M_vtrSCCOMP.remove(M_lblTODAT);
    		M_vtrSCCOMP.remove(M_txtTODAT);
    		M_vtrSCCOMP.remove(M_txtFMDAT);
    	    ButtonGroup bgrRPTOP = new ButtonGroup();
            add(new JLabel("Owner "),3,3,1,1,this,'L');
            add(cmbDPTCD = new JComboBox(),3,4,1,2,this,'L');
            add(new JLabel("Item Category "),4,3,1,1,this,'L');
            add(cmbITMCT = new JComboBox(),4,4,1,2,this,'L');
            M_pnlRPFMT.setVisible(true);
			setENBL(false);
			
            M_strSQLQRY = "Select CMT_CODCD,CMT_CGSTP,CMT_CODDS from CO_CDTRN WHERE CMT_CGMTP ='SYS'"+
				" AND CMT_CGSTP IN('COXXDPT','MMXXCTF') and isnull(CMT_STSFL,'') <>'X'";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
            if(M_rstRSSET !=null)
            {
                while(M_rstRSSET.next())
                {
                   if(M_rstRSSET.getString("CMT_CGSTP").equals("COXXDPT"))
                        cmbDPTCD.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));
                   else
                        cmbITMCT.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));
                }
                M_rstRSSET.close();
            }
           	add(new JLabel("Item Type "),5,3,1,1,this,'L');
           	add(rdoOPRCN = new JRadioButton("Reconditioned Items",true),5,4,1,1.5,this,'L'); // Option Indent wise
			add(rdoOPOTH = new JRadioButton("Others",false),5,6,1,1,this,'R');	// Option Date wise
			bgrRPTOP.add(rdoOPRCN);
			bgrRPTOP.add(rdoOPOTH);
	        hstPRTCD = new Hashtable<String,String>();
            M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST WHERE PT_PRTTP ='S'"
                          +" AND isnull(PT_STSFL,'') <>'X'";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
            if(M_rstRSSET !=null)
            {
                while(M_rstRSSET.next())               
                   hstPRTCD.put(M_rstRSSET.getString("PT_PRTCD"),nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"-"));                
                M_rstRSSET.close();
            }           
        }
        catch(Exception L_E)
        {
            setMSG(L_E,"constructor");
        }
    }
	/**
	 * Method to fetch data from the database & to club it with header & footer.
	 */
    void getDATA()
    {
        try
        {
            ResultSet L_rstRSSET;
            double L_dblYOSVL=0,L_dblTOTVL=0,L_dblGRPVL=0;
            int L_intITMCT =0;
            String L_strPGRPCD="",L_strGRPCD ="",L_strMATCD ="",L_strPRTNM="",L_strPRTCD="";
			
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Receipt Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			
			prnHEADER();
            M_strSQLQRY =" SELECT STP_MATCD,STP_UOMCD,STP_MATDS,STP_OWNBY,STP_CATFL,STP_YOSQT,STP_YOSVL,(day(getdate()) - day(ST_PISDT))L_DAYS,(day('06/30/2005') - day(ST_PISDT))L_DAYS1 "
                    +" ,ST_PPONO,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM,CTP_PRTCD FROM MM_STPRC,CO_CTMST,MM_STMST left outer join CO_CTPTR ON CTP_MATCD = ST_MATCD AND isnull(CTP_LPONO,'') = isnull(ST_PPONO,'') WHERE STP_CMPCD=ST_CMPCD AND STP_STRTP =ST_STRTP AND STP_MATCD = ST_MATCD AND STP_MATCD = CT_MATCD " 
                    + " and STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(STP_YOSQT,0) >0 and STP_STRTP ='"+M_strSBSCD.substring(2,4) +"'"
                    + " AND isnull(STP_CATFL,'') ='"+cmbITMCT.getSelectedItem().toString().substring(0,1)+"'"
                    + " AND isnull(STP_OWNBY,'') ='"+cmbDPTCD.getSelectedItem().toString().substring(0,3)+"' ";
			if(rdoOPRCN.isSelected())
				M_strSQLQRY += " AND SUBSTRING(STP_MATCD,10,1) IN('8','9') ";
            else 
				M_strSQLQRY += " AND SUBSTRING(STP_MATCD,10,1) NOT IN('8','9') ";
			M_strSQLQRY += " order by stp_matcd";
            intRECCT =0;
            L_dblTOTVL =0;
            L_dblGRPVL =0;
            M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
            if(M_rstRSSET !=null)
            {
                while(M_rstRSSET.next())
                {
                    L_strMATCD = M_rstRSSET.getString("STP_MATCD");
                    L_strGRPCD = L_strMATCD.substring(0,2);
                    if(intRECCT == 0)
                        L_strPGRPCD = L_strGRPCD;
                    if(!L_strGRPCD.equals(L_strPGRPCD))
                    {
                        dosREPORT.writeBytes("\n");
                        dosREPORT.writeBytes(padSTRING('R',"Total Items : "+L_intITMCT,75));
                        dosREPORT.writeBytes(padSTRING('L',"Total Value : "+setNumberFormat(L_dblGRPVL,2),59));
                        dosREPORT.writeBytes("\n\n");
                        cl_dat.M_intLINNO_pbst += 3;
                        L_strPGRPCD = L_strGRPCD;
                        L_intITMCT =0;
                        L_dblGRPVL =0;
                    }
                    L_strPRTNM ="-";
                    L_strPRTCD = nvlSTRVL(M_rstRSSET.getString("CTP_PRTCD"),"");
                    if(hstPRTCD.containsKey((String)L_strPRTCD))
                        L_strPRTNM = hstPRTCD.get(L_strPRTCD).toString();
                    dosREPORT.writeBytes(padSTRING('R',L_strMATCD,11));
                    dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_UOMCD"),""),4));
                    dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATDS"),""),55));
                    dosREPORT.writeBytes(padSTRING('R',L_strPRTNM,30));  
                    if((L_strMATCD.substring(9).equals("2"))||(L_strMATCD.substring(9).equals("6")))
                    {
                        dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_IILTM"),"0"),5));
                        dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_IELTM"),"0"),5));
                    }
                    else
                    {
                        dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),"0"),5));
                        dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),"0"),5));
                    }
                    dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_YOSQT"),"0"),12));
                    L_dblYOSVL = M_rstRSSET.getDouble("STP_YOSVL");
                    L_dblTOTVL += L_dblYOSVL; 
                    L_dblGRPVL += L_dblYOSVL; 
                    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblYOSVL,2),12));                  
                    if(M_rstRSSET.getInt("L_DAYS1")>0)
                        dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("L_DAYS1"),10));
                    else
                        dosREPORT.writeBytes(padSTRING('L',"-",10));
                    dosREPORT.writeBytes("\n");
                    cl_dat.M_intLINNO_pbst++;
                    intRECCT++;
                    L_intITMCT++;
                    if(cl_dat.M_intLINNO_pbst > 66)
				    {
    					dosREPORT.writeBytes("\n"+strDOTLN);					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
				    }   
                }
				M_rstRSSET.close();
            }
            if(intRECCT >0)
            {                
                dosREPORT.writeBytes(strDOTLN + "\n");
                dosREPORT.writeBytes(padSTRING('R',"Total Items : "+L_intITMCT,75));
                dosREPORT.writeBytes(padSTRING('L',"Total Value : "+setNumberFormat(L_dblGRPVL,2),59)+"\n\n");                
                dosREPORT.writeBytes(padSTRING('R',"Total Value of "+cmbITMCT.getSelectedItem().toString().substring(1)+ " Items for "+cmbDPTCD.getSelectedItem().toString().substring(3),75));
                dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTVL,2),59)+"\n");                
                if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					prnFMTCHR(dosREPORT,M_strEJT);
            }
            setMSG("Report completed.. ",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);
			}
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
        }
        catch(Exception L_E)
        {
            setMSG(L_E,"getDATA");
        }
   }
	/**
	 * Method to validate the data before the execuation of the SQL Query
	 */
   boolean vldDATA()
   {
	   cl_dat.M_PAGENO = 0;
	   if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
		{
			if (M_cmbDESTN.getItemCount() == 0)
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
	 * Method to generate the Report & to forward it to specified destination.
	 */
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		
		try
		{
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpstv.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpstv.doc";
			
			getDATA();
			
			if(intRECCT == 0)
			{	
				setMSG("No Data found..",'E');
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
					p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
					
				if(M_rdbHTML.isSelected())
				    p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
				else
				    p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM);
			}
			else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Owner / Category wise Stock Value Details"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}		
	} 
	/**
	 * Method to generate the header part of the Report.
	 */
    private void prnHEADER()
	{  
		try
		{			
			cl_dat.M_PAGENO ++;
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))		
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			
			dosREPORT.writeBytes("\n" + padSTRING('R',"SUPREME PETROCHEM LTD.",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type      : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));	
			dosREPORT.writeBytes("Page No.: " + String.valueOf(cl_dat.M_PAGENO) + "\n");
			dosREPORT.writeBytes("Stock Details report of "+cmbITMCT.getSelectedItem().toString().substring(1)+ " Items as on 30/06/2005 for "+cmbDPTCD.getSelectedItem().toString().substring(3)+"\n");
			dosREPORT.writeBytes(strDOTLN);
	    	dosREPORT.writeBytes("\nItem Code UOM Description                                             Supplier                      Lead Times  Yr.Op. Qty.      Value Days In Stk"+"\n");			
			dosREPORT.writeBytes(strDOTLN);
			dosREPORT.writeBytes("\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}	
    private void updDATA()
    {
       try
       {
             /* Updating the Ownership, STP_OWNBY */
        //    M_strSQLQRY = "UPDATE MM_STPRC SET STP_OWNBY =(SELECT CT_OWNBY FROM CO_CTMST WHERE CT_CODTP ='CD' AND CT_MATCD = STP_MATCD)";
         //   cl_dat.exeSQLUPD(M_strSQLQRY,"");
             
            /* Updating the Ownership, STP_OWNBY to 'X' :iunknown where category flag is blank*/
         //   M_strSQLQRY ="UPDATE MM_STPRC SET STP_OWNBY = 'X' where isnull(stp_ownby,'') =''" ;
          //  cl_dat.exeSQLUPD(M_strSQLQRY,"");
            
            /* Updating the category STP_CATFL */
            /* Initializing the category Flag */
            M_strSQLQRY = "update mm_stprc set stp_catfl = null";
            cl_dat.exeSQLUPD(M_strSQLQRY,"");
            
            /* Updating category flag for stock controlled item as 1 */
            M_strSQLQRY = "update mm_stprc set stp_catfl ='1' where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND stp_CMPCD + stp_strtp + stp_matcd in (select st_CMPCD + st_strtp + st_matcd from mm_stmst where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(st_stkfl,'') ='Y')";
            cl_dat.exeSQLUPD(M_strSQLQRY,"");
    
            /* Updating category flag for Insurance item as 2, overwriting the common item codes if any  */
            M_strSQLQRY = "update mm_stprc set stp_catfl ='2' where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND stp_CMPCD + stp_strtp + stp_matcd in ( select st_CMPCD + st_strtp + st_matcd from mm_stmst where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(st_vedfl,'') ='E')";
            cl_dat.exeSQLUPD(M_strSQLQRY,"");
    
            /* Updating category flag for Obsolete item as 3, overwriting the common item codes if any  */
            M_strSQLQRY = "update mm_stprc set stp_catfl ='3' where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND stp_CMPCD + stp_strtp + stp_matcd in ( select st_CMPCD + st_strtp + st_matcd from mm_stmst where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(st_stsfl,'') ='9')";
            cl_dat.exeSQLUPD(M_strSQLQRY,"");
        
            /* Updating category flag for Other item as 4, overwriting the common item codes if any  */
            M_strSQLQRY = "update mm_stprc set stp_catfl ='4' where STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND stp_catfl is null";
            cl_dat.exeSQLUPD(M_strSQLQRY,"");
            cl_dat.exeDBCMT("updDATA");
        }
        catch(Exception L_E)
        {
            setMSG(L_E,"updDATA");
		}   
	}    
}
