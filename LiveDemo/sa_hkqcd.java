/*
System Name   : Lebortory Information Management System
Program Name  : Database Cleanup Screen for all the test details 
                before the last three months from current date.
Program Desc. :
Author        : Mr.S.R.Mehesare
Date          : 14 JULY 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
/**<pre>
<B>System Name :</B> Lebortory Information Management System
 
<B>Program Name :</B> Test Data Cleanup Screen.

<B>Purpose :</B> Database Cleanup Screen for all the test details before 
three months from current date.

List of tables used :
Table Name  Primary key                             Operation done
                                            Insert  Update  Query  Delete	
-----------------------------------------------------------------------------
QC_RMMST    SMT_QCATP,SMT_TSTTP,SMT_TSTNO,
            SMT_MORTP,SMT_TSTDT,SMT_TSTRF                            #
QC_RSMST    RM_QCATP,RM_TSTTP,RM_TSTNO                               #
QC_SMTRN                                                             #
QC_WTTRN    WTT_QCATP,WTT_TSTTP,WTT_TSTNO,
            WTT_TSTDT,WTT_WTRTP                                      #
SA_PPUTR                                                             #
-----------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name     Table name         Type/Size    Description
-----------------------------------------------------------------------------
txtDATE     XXX_TSTDT       All Above Tables   Timestamp    Timestamp
-----------------------------------------------------------------------------

Validations :-
    - Given Date must be three  Months smaller than the Current Date as,
 deletation of test data within the last three months is not allowed.
*/

public class sa_hkqcd extends cl_pbase
{						/**JTextField to display & Enter Date to cleanup data before the given date.*/
	private JTextField txtDATE;		
	
	sa_hkqcd()
	{
		super(2);
		try
		{								
			setMatrix(20,8);
			add(new JLabel("Date"),3,3,1,.5,this,'R');
			add(txtDATE = new TxtDate(),3,4,1,1.2,this,'L');
			setENBL(false);
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
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)
			{
				txtDATE.setText("");				
				setMSG("Please Select an option ..",'N');
				txtDATE.setEnabled(false);
			}		
			else
			{				
				setMSG("Please Enter Date to delete Data before that date..",'N');
				txtDATE.requestFocus(); 				
				txtDATE.setEnabled(true);
			}
		}		
		else if(M_objSOURC == txtDATE)
		{
			if(txtDATE.getText().trim().length()>0)
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	
	/**
	 * Method to perform validy check for the Date entered, Before deleting data from the data base.
	 */
	boolean vldDATA()
	{
		try
		{
			if(txtDATE.getText().trim().length()==0)
			{
				setMSG("Please Enter Date to delete Data before that date..",'E');
				txtDATE.requestFocus();
				return false;
			}
			if (M_fmtLCDAT.parse(txtDATE.getText().trim()).compareTo(M_fmtLCDAT.parse(calDATE(cl_dat.M_strLOGDT_pbst)))>0)			
			{			    
				setMSG("Data within the last three month is not allowed to delete, Please Enter valid Date.. ",'E');								
				txtDATE.requestFocus();
				return false;			
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}
	/**
	 * Super class method overrided here to inhance its functionality, to perform 
	 * Database operations.
	 */
	void exeSAVE()
	{
		try
		{
			if(! vldDATA())
				return;
			else
				setMSG("",'N');
			
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strDATE = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDATE.getText().trim()));
			cl_dat.M_flgLCUPD_pbst = true;
			
			//----------------------------------------------------------------------------------
			M_strSQLQRY = "Delete from QC_RMMST where RM_TSTTP in ('0202','0203')";
			M_strSQLQRY += " AND RM_TSTNO in(select SMT_TSTNO from QC_SMTRN where SMT_QCATP ='01'";			
			M_strSQLQRY += " AND SMT_TSTTP in('0202','0203') AND CONVERT(varchar,SMT_TSTDT,101) < '"+L_strDATE +"')";			
			cl_dat.exeSQLUPD(M_strSQLQRY,"");	
			
			//---------------------Reactor sample details--------------------------------------------------------------				
			M_strSQLQRY = "delete from QC_RSMST where CONVERT(varchar,RS_TSTDT,101) < '"+ L_strDATE +"' and RS_TSTTP ='0201'";                 			
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			
			//---------------------REB and TBC details--------------------------------------------------------------
			M_strSQLQRY = "delete from QC_SMTRN where CONVERT(varchar,SMT_TSTDT,101) < '"+ L_strDATE +"' and SMT_TSTTP  in('0202','0203')";                 			
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			
			//----------------------------------------------------------------------------------
			M_strSQLQRY = "Delete from QC_RMMST where RM_TSTTP in('0401','0402','0403') and RM_TSTNO in ";
			M_strSQLQRY += "(select WTT_TSTNO from QC_WTTRN where CONVERT(varchar,WTT_TSTDT,101) < '"+L_strDATE +"')";			
			cl_dat.exeSQLUPD(M_strSQLQRY,"");				
			
			//----------------------------------------------------------------------------------
			M_strSQLQRY = "Delete from QC_WTTRN where CONVERT(varchar,WTT_TSTDT,101) < '"+ L_strDATE+"'";	    	
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			
			//----------------------------------------------------------------------------------	
			M_strSQLQRY = "Delete from SA_PPUTR where CONVERT(varchar,PPU_LGITM,101) < '"+getSTRDT()+"'";			
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			
			if(cl_dat.exeDBCMT("exeSAVE"))
			{				
				setMSG("Data Clean Up has been Completed..",'N');
				txtDATE.setText("");
			}
			else			
				setMSG("Error in Deleting data..",'N');			
			this.setCursor(cl_dat.M_curDFSTS_pbst);			
		}
		catch(Exception L_EX)
		{
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"exeSAVE");
		}
	}
	
	/**
	*Method to Calculate From-Date, one day smaller than To-Date.
    *@param P_strCURDT String argument to pass To_Date.
	*/
    private String calDATE(String P_strCURDT)
    {
        try
        {					
	        M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strCURDT));
		    M_calLOCAL.add(java.util.Calendar.DATE,-90);//+Integer.parseInt(P_strCURDT.substring(0,2))
       		return (M_fmtLCDAT.format(M_calLOCAL.getTime()));				            
		}
		catch(Exception L_EX)
		{	       
			setMSG(L_EX, "calDATE");
			return (cl_dat.M_strLOGDT_pbst);
        }					
	}
	private String getSTRDT() 
	{
		String L_strCRMNTH,L_strCRYRVL;		
		int L_intYRVAL,L_intPRMNTH ;
		String L_strPRMTH ="";
		String L_strSTRDT="";
		// Get Start date of the previous  month
		try
		{
			L_strCRMNTH = cl_dat.M_strLOGDT_pbst.substring(3,5);
			L_strCRYRVL = cl_dat.M_strLOGDT_pbst.substring(6,10);
			if(L_strCRMNTH.equals("01")) 
			{				
				//subtract the year value
				L_intYRVAL = Integer.valueOf(L_strCRYRVL).intValue()-1;
				L_strPRMTH = "12";
				L_strSTRDT = "01/"+"12/"+String.valueOf(L_intYRVAL);
			}
			else
			{
				L_strPRMTH ="";
				L_intPRMNTH = Integer.valueOf(L_strCRMNTH).intValue()-1;
				for(int i=0;i< 2- String.valueOf(L_intPRMNTH).length();i++)
				{
					L_strPRMTH += "0"   ;
				}
				L_strPRMTH +=String.valueOf(L_intPRMNTH);		 
				L_strSTRDT = "01"+"/" +L_strPRMTH +"/"+cl_dat.M_strLOGDT_pbst.substring(6,10);
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getSTRDT");
		}
		return L_strSTRDT;
	}
}
			
			