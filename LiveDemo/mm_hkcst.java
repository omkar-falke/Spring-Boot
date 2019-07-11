

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JOptionPane;import javax.swing.JComponent;
import javax.swing.JTable;import java.sql.ResultSet;
import java.util.StringTokenizer;import java.util.Hashtable;
import javax.swing.DefaultCellEditor;import javax.swing.JComboBox;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTabbedPane;import javax.swing.JPanel;import java.text.SimpleDateFormat;
import java.util.Date;import java.util.Calendar;import javax.swing.JButton;
import java.sql.PreparedStatement;
public class mm_hkcst extends cl_pbase
{
	JTextField txtYSTDT;
	JTextField txtPMEDT;
	JTextField txtRMEDT;
	JButton btnRUN;
	java.util.Date datTMPDT;
	SimpleDateFormat fmtDDMMYYYY=new SimpleDateFormat("dd/MM/yyyy");
	ResultSet rstRSSET_GRN_MOP,rstRSSET_ISS_MOP,rstRSSET_MRN_MOP,rstRSSET_SAN_MOP;
	ResultSet rstRSSET_GRN_MCL,rstRSSET_ISS_MCL,rstRSSET_MRN_MCL,rstRSSET_SAN_MCL;	
	PreparedStatement pstmGRN_MOP,pstmISS_MOP,pstmMRN_MOP,pstmSAN_MOP;
	PreparedStatement pstmGRN_MCL,pstmISS_MCL,pstmMRN_MCL,pstmSAN_MCL;
	mm_hkcst()
	{
		super(1);
		try
		{
			setMatrix(20,20);
			add(new JLabel("Year Start Date"),5,5,1,4,this,'L');
			add(txtYSTDT = new TxtDate(),5,9,1,3,this,'L');
			
			add(new JLabel("Previous Month End Date"),6,5,1,4,this,'L');
			add(txtPMEDT = new TxtDate(),6,9,1,3,this,'L');			
			
			add(new JLabel("Reporting Month End Date"),7,5,1,4,this,'L');
			add(txtRMEDT = new TxtDate(),7,9,1,3,this,'L');

			add(btnRUN=new JButton("RUN"),9,8,1,3,this,'L');
			
			txtYSTDT.setText("01/07/"+cl_dat.M_strLOGDT_pbst.substring(6,10));
			
			M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
			M_calLOCAL.add(Calendar.MONTH,-2);    
			datTMPDT = M_calLOCAL.getTime();
			txtPMEDT.setText(M_calLOCAL.getActualMaximum(Calendar.DAY_OF_MONTH)+fmtDDMMYYYY.format(datTMPDT).substring(2,10));
			
			M_calLOCAL.add(Calendar.MONTH,1);
			datTMPDT = M_calLOCAL.getTime();
			txtRMEDT.setText(M_calLOCAL.getActualMaximum(Calendar.DAY_OF_MONTH)+fmtDDMMYYYY.format(datTMPDT).substring(2,10));
			
			btnRUN.addKeyListener(this);

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
   void setENBL(boolean P_flgENBFL)
   {
		super.setENBL(P_flgENBFL);
   }
   public void exeHLPOK()
	{
		super.exeHLPOK();
		cl_dat.M_flgHELPFL_pbst = false;
    }

	public void keyPressed(KeyEvent L_KE)
	{
	    super.keyPressed(L_KE);
	    if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			try
			{
    		}
			catch(NullPointerException L_NPE)
			{
			    setMSG("Help not available",'N');                            
			}
		  }
	}
	void getDATA()
	{
	    try
	    {
   	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"getDATA");
	    }
	}

	boolean vldDATA()
	{
	    cl_dat.M_flgLCUPD_pbst = true;
		if(txtYSTDT.getText().length()<10)
		{
			setMSG("Please Enter Year Staring Date..",'E');
			txtYSTDT.requestFocus();
			return false;
		}
		if(txtPMEDT.getText().length()<10)
		{
			setMSG("Please Enter Previous Month End Date..",'E');
			txtPMEDT.requestFocus();
			return false;
		}
		if(txtRMEDT.getText().length()<10)
		{
			setMSG("Please Enter Reporting End Date..",'E');
			txtRMEDT.requestFocus();
			return false;
		}
	    return true;
	}

	void exeSAVE()
	{
	    try
	    {
	        if(!vldDATA())
	            return;
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"exeSAVE");
	    }
	}
	public void actionPerformed(ActionEvent L_AE)
    {
        super.actionPerformed(L_AE);
		if(M_objSOURC == btnRUN)   //// button verifies data entered
		{	
			if(!vldDATA())
				return;
			prcDATA();
		}
    }
	public void prcDATA()
	{
		try
		{
			String LP_CMPCD=cl_dat.M_strCMPCD_pbst;
			String LP_YSTDT=M_fmtDBDAT.format(M_fmtLCDAT.parse(txtYSTDT.getText()));
			String LP_PMEDT=M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPMEDT.getText()));
			String LP_RMEDT=M_fmtDBDAT.format(M_fmtLCDAT.parse(txtRMEDT.getText()));
			
			setMSG("Deleting data from mm_sttmp..",'N');
			M_strSQLQRY = " delete from mm_sttmp";
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
						
			setMSG("Insert data from mm_sttmp..",'N');
			M_strSQLQRY = " insert into mm_sttmp (stp_cmpcd,stp_strtp,stp_matcd,stp_matds,stp_yosqt,stp_uomcd,stp_mmsbs)  (select stp_cmpcd,stp_strtp,stp_matcd,stp_matds,stp_yosqt,stp_uomcd,stp_mmsbs from mm_stprc where stp_cmpcd = "+LP_CMPCD+")";
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			
			System.out.println("updating GRN_MOP");
			setMSG("Updating GRN_MOP..",'N');
			M_strSQLQRY = " select  GR_STRTP,rtrim(ltrim(GR_MATCD)) GR_MATCD, sum(GR_ACPQT) GR_RECQT from MM_GRMST";
			M_strSQLQRY+= " where GR_CMPCD = '"+LP_CMPCD+"' and  GR_ACPDT between '"+LP_YSTDT+"' and '"+LP_PMEDT+"' and GR_STSFL='2'  and isnull(GR_ACPQT,0) >0";
			M_strSQLQRY+= " group by GR_STRTP,GR_MATCD";
			M_strSQLQRY+= " ORDER BY GR_STRTP,GR_MATCD";
			rstRSSET_GRN_MOP = cl_dat.exeSQLQRY1(M_strSQLQRY);
			System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			
			
			pstmGRN_MOP = cl_dat.M_conSPDBA_pbst.prepareStatement("update mm_sttmp set STP_MORCQ = ? where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim('"+LP_CMPCD+"')) and rtrim(ltrim(stp_strtp)) = ? and rtrim(ltrim(stp_matcd)) = ?");
			if(rstRSSET_GRN_MOP!=null)
			{
				while(rstRSSET_GRN_MOP.next())
				{
					pstmGRN_MOP.setString(1,rstRSSET_GRN_MOP.getString("GR_RECQT"));
					pstmGRN_MOP.setString(2,rstRSSET_GRN_MOP.getString("GR_STRTP"));
					pstmGRN_MOP.setString(3,rstRSSET_GRN_MOP.getString("GR_MATCD"));
					pstmGRN_MOP.executeUpdate();
				}	
			}
			System.out.println("updated GRN_MOP");
			
			if(rstRSSET_GRN_MOP!=null)
				rstRSSET_GRN_MOP.close();
			
			System.out.println("updating ISS_MOP");
			setMSG("Updating ISS_MOP..",'N');
			M_strSQLQRY = " select  IS_STRTP,rtrim(ltrim(IS_MATCD)) IS_MATCD, sum(IS_ISSQT) IS_ISSQT from MM_ISMST";
			M_strSQLQRY+= " where IS_CMPCD = '"+LP_CMPCD+"'  and CONVERT(varchar,IS_AUTDT,103) between '"+LP_YSTDT+"' and '"+LP_PMEDT+"'  and IS_STSFL='2' and isnull(IS_ISSQT,0) >0";
			M_strSQLQRY+= " group by IS_STRTP,IS_MATCD";
			M_strSQLQRY+= " ORDER BY IS_STRTP,IS_MATCD";
			rstRSSET_ISS_MOP = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			pstmISS_MOP = cl_dat.M_conSPDBA_pbst.prepareStatement("update mm_sttmp set STP_MOISQ = ? where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim('"+LP_CMPCD+"')) and rtrim(ltrim(stp_strtp)) = ? and rtrim(ltrim(stp_matcd)) = ?");
			if(rstRSSET_ISS_MOP!=null)
			{
				while(rstRSSET_ISS_MOP.next())
				{
					pstmISS_MOP.setString(1,rstRSSET_ISS_MOP.getString("IS_ISSQT"));
					pstmISS_MOP.setString(2,rstRSSET_ISS_MOP.getString("IS_STRTP"));
					pstmISS_MOP.setString(3,rstRSSET_ISS_MOP.getString("IS_MATCD"));
					pstmISS_MOP.executeUpdate();
				}	
			}
			System.out.println("updated ISS_MOP");
			
			if(rstRSSET_ISS_MOP!=null)
				rstRSSET_ISS_MOP.close();
			
			System.out.println("updating MRN_MOP");
			setMSG("Updating MRN_MOP..",'N');
			M_strSQLQRY = " select MR_STRTP,rtrim(ltrim(MR_MATCD)) MR_MATCD, sum(MR_RETQT) MR_RETQT from MM_MRMST";
			M_strSQLQRY+= " where MR_CMPCD = '"+LP_CMPCD+"' and  MR_AUTDT between '"+LP_YSTDT+"' and '"+LP_PMEDT+"'  and MR_STSFL='5' and isnull(MR_RETQT,0) >0";
			M_strSQLQRY+= " group by MR_STRTP,MR_MATCD";
			M_strSQLQRY+= " ORDER BY MR_STRTP,MR_MATCD";
			rstRSSET_MRN_MOP = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			pstmMRN_MOP = cl_dat.M_conSPDBA_pbst.prepareStatement("update mm_sttmp set STP_MOMRQ = ? where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim('"+LP_CMPCD+"')) and rtrim(ltrim(stp_strtp)) = ? and rtrim(ltrim(stp_matcd)) = ?");
			if(rstRSSET_MRN_MOP!=null)
			{
				while(rstRSSET_MRN_MOP.next())
				{
					pstmMRN_MOP.setString(1,rstRSSET_MRN_MOP.getString("MR_RETQT"));
					pstmMRN_MOP.setString(2,rstRSSET_MRN_MOP.getString("MR_STRTP"));
					pstmMRN_MOP.setString(3,rstRSSET_MRN_MOP.getString("MR_MATCD"));
					pstmMRN_MOP.executeUpdate();
				}	
			}
			System.out.println("updated MRN_MOP");
			
			if(rstRSSET_MRN_MOP!=null)
				rstRSSET_MRN_MOP.close();
			
			System.out.println("updating SAN_MOP");
			setMSG("Updating SAN_MOP..",'N');
			M_strSQLQRY = " select  SA_STRTP,rtrim(ltrim(SA_MATCD)) SA_MATCD, sum(SA_SANQT) SA_SANQT from MM_SAMST";
			M_strSQLQRY+= " where SA_CMPCD = '"+LP_CMPCD+"' and SA_SANDT between '"+LP_YSTDT+"' and '"+LP_PMEDT+"' and SA_STSFL<>'X' and isnull(SA_SANQT,0) >0";
			M_strSQLQRY+= " group by SA_STRTP,SA_MATCD";
			M_strSQLQRY+= " ORDER BY SA_STRTP,SA_MATCD";
			rstRSSET_SAN_MOP = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			pstmSAN_MOP = cl_dat.M_conSPDBA_pbst.prepareStatement("update mm_sttmp set STP_MOSAQ = ? where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim('"+LP_CMPCD+"')) and rtrim(ltrim(stp_strtp)) = ? and rtrim(ltrim(stp_matcd)) = ?");
			if(rstRSSET_SAN_MOP!=null)
			{
				while(rstRSSET_SAN_MOP.next())
				{
					pstmSAN_MOP.setString(1,rstRSSET_SAN_MOP.getString("SA_SANQT"));
					pstmSAN_MOP.setString(2,rstRSSET_SAN_MOP.getString("SA_STRTP"));
					pstmSAN_MOP.setString(3,rstRSSET_SAN_MOP.getString("SA_MATCD"));
					pstmSAN_MOP.executeUpdate();
				}	
			}		
			System.out.println("updated SAN_MOP");
			
			if(rstRSSET_SAN_MOP!=null)
				rstRSSET_SAN_MOP.close();
			
			System.out.println("updating GRN_MCL");
			setMSG("Updating GRN_MCL..",'N');
			M_strSQLQRY = " select  GR_STRTP,rtrim(ltrim(GR_MATCD)) GR_MATCD, sum(GR_ACPQT) GR_RECQT from MM_GRMST";
			M_strSQLQRY+= " where GR_CMPCD = '"+LP_CMPCD+"' and  GR_ACPDT between '"+LP_YSTDT+"' and '"+LP_RMEDT+"' and GR_STSFL='2' and isnull(GR_ACPQT,0) >0";
			M_strSQLQRY+= " group by GR_STRTP,GR_MATCD";
			M_strSQLQRY+= " ORDER BY GR_STRTP,GR_MATCD";
			rstRSSET_GRN_MCL = cl_dat.exeSQLQRY1(M_strSQLQRY);	
			
			pstmGRN_MCL = cl_dat.M_conSPDBA_pbst.prepareStatement("update mm_sttmp set STP_MCRCQ = ? where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim('"+LP_CMPCD+"')) and rtrim(ltrim(stp_strtp)) = ? and rtrim(ltrim(stp_matcd)) = ?");
			if(rstRSSET_GRN_MCL!=null)
			{
				while(rstRSSET_GRN_MCL.next())
				{
					pstmGRN_MCL.setString(1,rstRSSET_GRN_MCL.getString("GR_RECQT"));
					pstmGRN_MCL.setString(2,rstRSSET_GRN_MCL.getString("GR_STRTP"));
					pstmGRN_MCL.setString(3,rstRSSET_GRN_MCL.getString("GR_MATCD"));
					pstmGRN_MCL.executeUpdate();
				}	
			}
			System.out.println("updated GRN_MCL");
			
			if(rstRSSET_GRN_MCL!=null)
				rstRSSET_GRN_MCL.close();
			
			System.out.println("updating ISS_MCL");
			setMSG("Updating ISS_MCL..",'N');
			M_strSQLQRY = " select IS_STRTP,rtrim(ltrim(IS_MATCD)) IS_MATCD, sum(IS_ISSQT) IS_ISSQT from MM_ISMST";
			M_strSQLQRY+= " where IS_CMPCD = '"+LP_CMPCD+"' and CONVERT(varchar,IS_AUTDT,103) between '"+LP_YSTDT+"' and '"+LP_PMEDT+"'  and IS_STSFL='2' and isnull(IS_ISSQT,0) >0";
			M_strSQLQRY+= " group by IS_STRTP,IS_MATCD";
			M_strSQLQRY+= " ORDER BY IS_STRTP,IS_MATCD";
			rstRSSET_ISS_MCL = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			pstmISS_MCL = cl_dat.M_conSPDBA_pbst.prepareStatement("update mm_sttmp set STP_MCISQ = ? where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim('"+LP_CMPCD+"')) and rtrim(ltrim(stp_strtp)) = ? and rtrim(ltrim(stp_matcd)) = ?");
			if(rstRSSET_ISS_MCL!=null)
			{
				while(rstRSSET_ISS_MCL.next())
				{
					pstmISS_MCL.setString(1,rstRSSET_ISS_MCL.getString("IS_ISSQT"));
					pstmISS_MCL.setString(2,rstRSSET_ISS_MCL.getString("IS_STRTP"));
					pstmISS_MCL.setString(3,rstRSSET_ISS_MCL.getString("IS_MATCD"));
					pstmISS_MCL.executeUpdate();
				}	
			}		
			
			System.out.println("updated ISS_MCL");
			
			if(rstRSSET_ISS_MCL!=null)
				rstRSSET_ISS_MCL.close();
			
			System.out.println("updating MRN_MCL");
			setMSG("Updating MRN_MCL..",'N');
			M_strSQLQRY = " select  MR_STRTP,rtrim(ltrim(MR_MATCD)) MR_MATCD, sum(MR_RETQT) MR_RETQT from MM_MRMST";
			M_strSQLQRY+= " where MR_CMPCD = '"+LP_CMPCD+"' and  MR_AUTDT between '"+LP_YSTDT+"' and '"+LP_RMEDT+"'  and MR_STSFL='5' and isnull(MR_RETQT,0) >0";
			M_strSQLQRY+= " group by MR_STRTP,MR_MATCD";
			M_strSQLQRY+= " ORDER BY MR_STRTP,MR_MATCD";
			rstRSSET_MRN_MCL = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			pstmMRN_MCL = cl_dat.M_conSPDBA_pbst.prepareStatement("update mm_sttmp set STP_MCMRQ = ? where rtrim(ltrim(stp_cmpcd)) =rtrim(ltrim('"+LP_CMPCD+"')) and rtrim(ltrim(stp_strtp)) = ? and rtrim(ltrim(stp_matcd)) = ?");
			if(rstRSSET_MRN_MCL!=null)
			{
				while(rstRSSET_MRN_MCL.next())
				{
					pstmMRN_MCL.setString(1,rstRSSET_MRN_MCL.getString("MR_RETQT"));
					pstmMRN_MCL.setString(2,rstRSSET_MRN_MCL.getString("MR_STRTP"));
					pstmMRN_MCL.setString(3,rstRSSET_MRN_MCL.getString("MR_MATCD"));
					pstmMRN_MCL.executeUpdate();
				}	
			}		
			
			System.out.println("updated MRN_MCL");
			
			if(rstRSSET_MRN_MCL!=null)
				rstRSSET_MRN_MCL.close();
			
			System.out.println("updating SAN_MCL");
			setMSG("Updating SAN_MCL..",'N');
			M_strSQLQRY = " select  SA_STRTP,rtrim(ltrim(SA_MATCD)) SA_MATCD, sum(SA_SANQT) SA_SANQT from MM_SAMST";
			M_strSQLQRY+= " where SA_CMPCD = '"+LP_CMPCD+"' and SA_SANDT between '"+LP_YSTDT+"' and '"+LP_RMEDT+"' and SA_STSFL<>'X' and isnull(SA_SANQT,0) >0";
			M_strSQLQRY+= " group by SA_STRTP,SA_MATCD";
			M_strSQLQRY+= " ORDER BY SA_STRTP,SA_MATCD";
			rstRSSET_SAN_MCL = cl_dat.exeSQLQRY1(M_strSQLQRY);			
			
			pstmSAN_MCL = cl_dat.M_conSPDBA_pbst.prepareStatement("update mm_sttmp set STP_MCSAQ = ? where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim('"+LP_CMPCD+"')) and rtrim(ltrim(stp_strtp)) = ? and rtrim(ltrim(stp_matcd)) = ?");
			if(rstRSSET_SAN_MCL!=null)
			{
				while(rstRSSET_SAN_MCL.next())
				{
					pstmSAN_MCL.setString(1,rstRSSET_SAN_MCL.getString("SA_SANQT"));
					pstmSAN_MCL.setString(2,rstRSSET_SAN_MCL.getString("SA_STRTP"));
					pstmSAN_MCL.setString(3,rstRSSET_SAN_MCL.getString("SA_MATCD"));
					pstmSAN_MCL.executeUpdate();
				}	
			}			
			System.out.println("updated SAN_MCL");
			
			if(rstRSSET_SAN_MCL!=null)
				rstRSSET_SAN_MCL.close();
			
			System.out.println("updating STP_MOSQT");
			setMSG("Updating STP_MOSQT..",'N');
		    M_strSQLQRY=" update mm_sttmp set STP_MOSQT = isnull(STP_YOSQT,0)+isnull(STP_MORCQ,0)+isnull(STP_MOMRQ,0)-isnull(STP_MOISQ,0)+isnull(STP_MOSAQ,0) where stp_cmpcd='"+LP_CMPCD+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			System.out.println("updated STP_MOSQT");
			
			System.out.println("updating STP_MCSQT");
			setMSG("Updating STP_MCSQT..",'N');
			M_strSQLQRY=" update mm_sttmp set STP_MCSQT = isnull(STP_YOSQT,0)+isnull(STP_MCRCQ,0)+isnull(STP_MCMRQ,0)-isnull(STP_MCISQ,0)+isnull(STP_MCSAQ,0) where stp_cmpcd='"+LP_CMPCD+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			System.out.println("updated STP_MCSQT");
			
			cl_dat.exeDBCMT("exeUPDTBLS");			
		}
		catch(Exception E)
		{
			System.out.println("inside prcDATA() : "+E);
		}	
	}
}
